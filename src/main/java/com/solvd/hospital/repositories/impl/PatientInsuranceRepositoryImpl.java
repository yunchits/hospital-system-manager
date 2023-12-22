package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;
import com.solvd.hospital.repositories.PatientInsuranceRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PatientInsuranceRepositoryImpl implements PatientInsuranceRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_INSURANCE_QUERY = "INSERT INTO patient_insurances (patient_id, policy_number, expiration_date, coverage_amount, type, insurance_provider) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_INSURANCE_BY_ID_QUERY = "SELECT * FROM patient_insurances WHERE patient_id = ?";
    private static final String UPDATE_INSURANCE_QUERY = "UPDATE patient_insurances SET policy_number = ?, expiration_date = ?, coverage_amount = ?, type = ?, insurance_provider = ? WHERE patient_id = ?";
    private static final String DELETE_INSURANCE_QUERY = "DELETE FROM patient_insurances WHERE patient_id = ?";


    @Override
    public Insurance create(Insurance insurance) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_INSURANCE_QUERY)) {

            statement.setLong(1, insurance.getPatientId());
            statement.setString(2, insurance.getPolicyNumber());
            statement.setDate(3, Date.valueOf(insurance.getExpirationDate()));
            statement.setDouble(4, insurance.getCoverageAmount());
            statement.setString(5, insurance.getType().name());
            statement.setString(6, insurance.getInsuranceProvider());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating insurance failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating insurance", e);
        }
        return insurance;
    }

    @Override
    public Optional<Insurance> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_INSURANCE_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToInsurance(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Insurance update(Insurance insurance) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_INSURANCE_QUERY)) {

            statement.setString(1, insurance.getPolicyNumber());
            statement.setDate(2, Date.valueOf(insurance.getExpirationDate()));
            statement.setDouble(3, insurance.getCoverageAmount());
            statement.setString(4, insurance.getType().name());
            statement.setString(5, insurance.getInsuranceProvider());
            statement.setLong(6, insurance.getPatientId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating insurance failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating insurance", e);
        }
        return insurance;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_INSURANCE_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting insurance", e);
        }
    }

    private Insurance resultSetToInsurance(ResultSet resultSet) throws SQLException {
        return new Insurance()
            .setPatientId(resultSet.getLong("patient_id"))
            .setPolicyNumber(resultSet.getString("policy_number"))
            .setExpirationDate(resultSet.getDate("expiration_date").toLocalDate())
            .setCoverageAmount(resultSet.getDouble("coverage_amount"))
            .setType(InsuranceType.valueOf(resultSet.getString("type")))
            .setInsuranceProvider(resultSet.getString("insurance_provider"));
    }
}
