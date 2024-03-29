package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.DataAccessException;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.dao.PatientDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPatientDAOImpl implements PatientDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PATIENT_WITH_USER_QUERY = "INSERT INTO patients (first_name, last_name, birth_date, gender, user_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String CREATE_PATIENT_QUERY = "INSERT INTO patients (first_name, last_name, birth_date, gender) " +
            "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_PATIENTS_QUERY = "SELECT * FROM patients";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM patients WHERE id = ?";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM patients WHERE user_id = ?";
    private static final String UPDATE_PATIENT_QUERY = "UPDATE patients " +
        "SET first_name = ?, last_name = ?, birth_date = ?, gender = ?  WHERE id = ?";
    private static final String UPDATE_PATIENT_USER_ID_QUERY = "UPDATE patients SET user_id = ? WHERE id = ?";
    private static final String DELETE_PATIENT_BY_ID_QUERY = "DELETE FROM patients WHERE id = ?";


    @Override
    public Patient createWithUser(Patient patient) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_PATIENT_WITH_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setDate(3, Date.valueOf(patient.getBirthDate()));
            statement.setString(4, patient.getGender().name());
            statement.setLong(5, patient.getUserId());

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
            throw new DataAccessException("Error creating patient", e);
        }
        return patient;
    }

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
            throw new DataAccessException("Error creating patient", e);
        }
        return patient;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PATIENTS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                patients.add(resultSetToPatient(resultSet));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error getting all patients.", e);
        }
        return patients;
    }

    @Override
    public Optional<Patient> findById(long id) {
        return findById(id, FIND_BY_ID_QUERY);
    }

    @Override
    public Optional<Patient> findByUserId(long userId) {
        return findById(userId, FIND_BY_USER_ID_QUERY);
    }

    @Override
    public Patient update(Patient patient) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PATIENT_QUERY)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setDate(3, Date.valueOf(patient.getBirthDate()));
            statement.setString(4, patient.getGender().name());
            statement.setLong(5, patient.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating doctor", e);
        }
        return patient;
    }

    @Override
    public Patient updateUserId(Patient patient) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PATIENT_USER_ID_QUERY)) {

            statement.setLong(1, patient.getUserId());
            statement.setLong(2, patient.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating doctor", e);
        }
        return patient;
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PATIENT_BY_ID_QUERY)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private Optional<Patient> findById(long id, String query) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToPatient(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    private Patient resultSetToPatient(ResultSet resultSet) throws SQLException {
        return new Patient()
                .setId(resultSet.getLong("id"))
                .setUserId(resultSet.getLong("user_id"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setBirthDate(resultSet.getDate("birth_date").toLocalDate())
                .setGender(Gender.valueOf(resultSet.getString("gender")));
    }
}
