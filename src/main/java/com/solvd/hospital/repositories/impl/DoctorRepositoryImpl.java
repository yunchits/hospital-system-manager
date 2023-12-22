package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.repositories.DoctorRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepositoryImpl implements DoctorRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_DOCTOR_QUERY = "INSERT INTO doctors (first_name, last_name, specialization, salary_id) " +
        "VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_DOCTOR_QUERY = "SELECT * FROM doctors";
    private static final String GET_DOCTOR_BY_ID_QUERY = "SELECT * FROM doctors WHERE id = ?";
    private static final String DELETE_DOCTOR_BY_ID_QUERY = "DELETE FROM doctors WHERE id = ?";

    @Override
    public Doctor create(Doctor doctor) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_DOCTOR_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, doctor.getFirstName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getSpecialization());
            statement.setLong(4, doctor.getSalary().getId());


            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    doctor.setId(id);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor", e);
        }
        return doctor;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_DOCTOR_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                doctors.add(resultSetToDoctor(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all doctors.", e);
        }
        return doctors;
    }

    @Override
    public Optional<Doctor> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DOCTOR_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToDoctor(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Doctor update(Doctor doctor) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DOCTOR_BY_ID_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Doctor resultSetToDoctor(ResultSet resultSet) throws SQLException {
        return new Doctor()
            .setId(resultSet.getLong("id"))
            .setFirstName(resultSet.getString("first_name"))
            .setLastName(resultSet.getString("last_name"))
            .setSpecialization(resultSet.getString("specialization"));
    }
}
