package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.repositories.DiagnosisRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DiagnosisRepositoryImpl implements DiagnosisRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_DIAGNOSIS_QUERY = "INSERT INTO diagnosis (diagnosis_name, diagnosis_description) " +
        "VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM diagnosis WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM diagnosis WHERE diagnosis_name = ?";
    private static final String DELETE_DIAGNOSIS_QUERY = "DELETE FROM diagnosis WHERE id = ?";

    @Override
    public Diagnosis create(Diagnosis diagnosis) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_DIAGNOSIS_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, diagnosis.getDiagnosisName());
            statement.setString(2, diagnosis.getDiagnosisDescription());

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
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DIAGNOSIS_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting diagnosis", e);
        }
    }

    private Diagnosis resultSetToDiagnosis(ResultSet resultSet) throws SQLException {
        return new Diagnosis()
            .setId(resultSet.getLong("id"))
            .setDiagnosisName(resultSet.getString("diagnosis_name"))
            .setDiagnosisDescription(resultSet.getString("diagnosis_description"));
    }
}
