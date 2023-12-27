package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.PatientService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCAppointmentDAOImpl implements AppointmentDAO {

    public static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_APPOINTMENT_QUERY = "INSERT INTO appointments (patient_id, doctor_id, appointment_datetime) " +
        "VALUES (?, ?, ?)";
    private static final String FIND_ALL_APPOINTMENTS_QUERY = "SELECT * FROM appointments";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM appointments WHERE id = ?";
    private static final String FIND_APPOINTMENTS_BY_PATIENT_ID_QUERY = "SELECT * FROM appointments WHERE patient_id = ?";
    private static final String FIND_APPOINTMENTS_BY_DOCTOR_ID_QUERY = "SELECT * FROM appointments WHERE doctor_id = ?";
    private static final String UPDATE_APPOINTMENT_QUERY = "UPDATE appointments " +
        "SET patient_id = ?, doctor_id = ?, appointment_datetime = ?  WHERE id = ?";
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointments WHERE id = ?";
    private static final String DELETE_APPOINTMENT_BY_PATIENT_ID_QUERY = "DELETE FROM appointments WHERE patient_id = ?";

    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    @Override
    public Appointment create(Appointment appointment) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_APPOINTMENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, appointment.getPatient().getId());
            statement.setLong(2, appointment.getDoctor().getId());
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
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_APPOINTMENTS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                appointments.add(resultSetToAppointment(resultSet));
            }

        } catch (SQLException | EntityNotFoundException e) {
            throw new RuntimeException("Error getting appointments", e);
        }

        return appointments;
    }

    @Override
    public Optional<Appointment> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToAppointment(resultSet));
                }
            }

        } catch (SQLException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findByPatientId(long patientId) {
        return findAppointmentsById(patientId, FIND_APPOINTMENTS_BY_PATIENT_ID_QUERY);
    }

    @Override
    public List<Appointment> findByDoctorId(long doctorId) {
        return findAppointmentsById(doctorId, FIND_APPOINTMENTS_BY_DOCTOR_ID_QUERY);
    }

    @Override
    public Appointment update(Appointment appointment) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_APPOINTMENT_QUERY)) {

            statement.setLong(1, appointment.getPatient().getId());
            statement.setLong(2, appointment.getDoctor().getId());
            statement.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            statement.setLong(4, appointment.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating appointment failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
        return appointment;
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_APPOINTMENT)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByPatient(long patientId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_APPOINTMENT_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Appointment> findAppointmentsById(long id, String query) {
        List<Appointment> appointments = new ArrayList<>();
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(resultSetToAppointment(resultSet));
                }
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointments;
    }

    private Appointment resultSetToAppointment(ResultSet resultSet) throws SQLException, EntityNotFoundException {
        return new Appointment()
            .setId(resultSet.getLong("id"))
            .setPatient(patientService.findById(resultSet.getLong("patient_id")))
            .setDoctor(doctorService.findById(resultSet.getLong("doctor_id")))
            .setAppointmentDateTime(resultSet.getTimestamp("appointment_datetime").toLocalDateTime());
    }
}
