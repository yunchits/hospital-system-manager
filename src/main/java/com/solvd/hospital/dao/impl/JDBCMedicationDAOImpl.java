package com.solvd.hospital.dao.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.dao.MedicationDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCMedicationDAOImpl implements MedicationDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_MEDICATION_QUERY = "INSERT INTO medications (medication_name, medication_description) VALUES (?, ?)";
    private static final String GET_ALL_MEDICATION_QUERY = "SELECT * FROM medications";
    private static final String GET_MEDICATION_BY_ID_QUERY = "SELECT * FROM medications WHERE id = ?";
    private static final String UPDATE_MEDICATION_QUERY = "UPDATE medications SET medication_name = ?, medication_description = ? WHERE id = ?";
    private static final String DELETE_MEDICATION_QUERY = "DELETE FROM medications WHERE id = ?";

    @Override
    public Medication create(Medication medication) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_MEDICATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, medication.getName());
            statement.setString(2, medication.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating medication failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    medication.setId(id);
                } else {
                    throw new SQLException("Creating medication failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating medication", e);
        }
        return medication;
    }

    @Override
    public List<Medication> findAll() {
        List<Medication> medications = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_MEDICATION_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                medications.add(resultSetToMedication(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all medications.", e);
        }
        return medications;
    }

    @Override
    public Optional<Medication> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDICATION_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToMedication(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Medication update(Medication medication) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MEDICATION_QUERY)) {

            statement.setString(1, medication.getName());
            statement.setString(2, medication.getDescription());
            statement.setLong(3, medication.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating medication failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating medication", e);
        }
        return medication;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEDICATION_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting medication", e);
        }
    }

    private Medication resultSetToMedication(ResultSet resultSet) throws SQLException {
        return new Medication()
            .setId(resultSet.getLong("id"))
            .setName(resultSet.getString("medication_name"))
            .setDescription(resultSet.getString("medication_description"));
    }
}
