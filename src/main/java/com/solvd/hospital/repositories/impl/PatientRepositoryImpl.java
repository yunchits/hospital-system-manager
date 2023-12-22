package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.repositories.PatientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PATIENT_QUERY = "INSERT INTO patients (first_name, last_name, birth_date, gender) " +
            "VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_PATIENTS_QUERY = "SELECT * FROM patients";
    private static final String GET_PATIENT_BY_ID_QUERY = "SELECT * FROM patients WHERE id = ?";
    private static final String DELETE_PATIENT_BY_ID_QUERY = "DELETE FROM patients WHERE id = ?";


    @Override
    public Patient create(Patient patient) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_PATIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setDate(3, Date.valueOf(patient.getBirthDate()));
            statement.setString(4, patient.getGender().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    patient.setId(id);
                } else {
                    throw new SQLException("Creating patient failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating patient", e);
        }
        return patient;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_PATIENTS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                patients.add(resultSetToPatient(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all patients.", e);
        }
        return patients;
    }

    @Override
    public Optional<Patient> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PATIENT_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToPatient(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PATIENT_BY_ID_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Patient resultSetToPatient(ResultSet resultSet) throws SQLException {
        return new Patient()
                .setId(resultSet.getLong("id"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setBirthDate(resultSet.getDate("birth_date").toLocalDate())
                .setGender(Gender.valueOf(resultSet.getString("gender")));
    }
}
