package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.repositories.HospitalizationRepository;
import com.solvd.hospital.services.impl.PatientServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalizationRepositoryImpl implements HospitalizationRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_HOSPITALIZATION_QUERY = "INSERT INTO hospitalizations (patient_id, admission_date, discharge_date) " +
            "VALUES (?, ?, ?)";
    private static final String GET_HOSPITALIZATION_BY_PATIENT_ID_QUERY = "SELECT * FROM hospitalizations WHERE patient_id = ?";
    private static final String DELETE_HOSPITALIZATION_BY_ID_QUERY = "DELETE FROM hospitalizations WHERE id = ?";

    private final PatientServiceImpl patientService = new PatientServiceImpl();

    @Override
    public Hospitalization create(Hospitalization hospitalization) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_HOSPITALIZATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, hospitalization.getPatient().getId());
            statement.setDate(2, Date.valueOf(hospitalization.getAdmissionDate()));
            statement.setDate(3, Date.valueOf(hospitalization.getDischargeDate()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating hospitalization failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    hospitalization.setId(id);
                } else {
                    throw new SQLException("Creating hospitalization failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating hospitalization", e);
        }
        return hospitalization;
    }

    @Override
    public List<Hospitalization> findByPatientId(long patientId) {
        List<Hospitalization> hospitalizations = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HOSPITALIZATION_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hospitalizations.add(resultSetToHospitalization(resultSet));
                }
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hospitalizations;
    }

    @Override
    public boolean delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_HOSPITALIZATION_BY_ID_QUERY)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting hospitalization", e);
        }
    }

    private Hospitalization resultSetToHospitalization(ResultSet resultSet) throws SQLException, NotFoundException {
        return new Hospitalization()
                .setId(resultSet.getLong("id"))
                .setPatient(patientService.findById(resultSet.getLong("patient_id")))
                .setAdmissionDate(resultSet.getDate("admission_date").toLocalDate())
                .setDischargeDate(resultSet.getDate("discharge_date").toLocalDate());
    }
}
