package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.dao.DiagnosisDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCDiagnosisDAOImpl implements DiagnosisDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_DIAGNOSIS_QUERY = "INSERT INTO diagnoses (diagnosis_name, diagnosis_description) " +
        "VALUES (?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM diagnoses";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM diagnoses WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM diagnoses WHERE diagnosis_name = ?";
    private static final String UPDATE_DIAGNOSIS_QUERY = "UPDATE diagnoses " +
        "SET diagnosis_name = ?, diagnosis_description = ? WHERE id = ?";
    private static final String DELETE_DIAGNOSIS_QUERY = "DELETE FROM diagnoses WHERE id = ?";

    @Override
    public Diagnosis create(Diagnosis diagnosis) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_DIAGNOSIS_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, diagnosis.getName());
            statement.setString(2, diagnosis.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating diagnosis failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    diagnosis.setId(id);
                } else {
                    throw new SQLException("Creating diagnosis failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating diagnosis", e);
        }
        return diagnosis;
    }

    @Override
    public List<Diagnosis> findAll() {
        List<Diagnosis> diagnoses = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                diagnoses.add(resultSetToDiagnosis(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing diagnosis query", e);
        }
        return diagnoses;
    }

    @Override
    public Optional<Diagnosis> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToDiagnosis(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting patient diagnoses by patient ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Diagnosis> findByDiagnosisName(String diagnosesName) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_QUERY)) {

            statement.setString(1, diagnosesName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToDiagnosis(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting patient diagnoses by patient ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Diagnosis update(Diagnosis diagnosis) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DIAGNOSIS_QUERY)) {

            statement.setString(1, diagnosis.getName());
            statement.setString(2, diagnosis.getDescription());
            statement.setLong(3, diagnosis.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating diagnosis failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating diagnosis", e);
        }
        return diagnosis;
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DIAGNOSIS_QUERY)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting diagnosis", e);
        }
    }

    private Diagnosis resultSetToDiagnosis(ResultSet resultSet) throws SQLException {
        return new Diagnosis()
            .setId(resultSet.getLong("id"))
            .setName(resultSet.getString("diagnosis_name"))
            .setDescription(resultSet.getString("diagnosis_description"));
    }
}
