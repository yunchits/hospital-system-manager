package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.repositories.AppointmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    public static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_APPOINTMENT_QUERY = "INSERT INTO appointments (patient_id, doctor_id, appointment_datetime) " +
            "VALUES (?, ?, ?)";
    private static final String GET_APPOINTMENT_BY_ID_QUERY = "SELECT * FROM appointments WHERE patient_id = ?";
    private static final String DELETE_APPOINTMENT_BY_ID_QUERY = "DELETE FROM patients WHERE id = ?";

    @Override
    public Appointment create(Appointment appointment) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_APPOINTMENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, appointment.getPatientId());
            statement.setLong(2, appointment.getDoctorId());
            statement.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDateTime()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    appointment.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointment;
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        List<Appointment> appointments = new ArrayList<>();
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_APPOINTMENT_BY_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(resultSetToAppointment(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointments;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_APPOINTMENT_BY_ID_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Appointment resultSetToAppointment(ResultSet resultSet) throws SQLException {
        return new Appointment()
                .setId(resultSet.getLong("id"))
                .setPatientId(resultSet.getLong("patient_id"))
                .setDoctorId(resultSet.getLong("doctor_id"))
                .setAppointmentDateTime(resultSet.getTimestamp("appointment_datetime").toLocalDateTime());
    }
}
