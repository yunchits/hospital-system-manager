package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.repositories.PatientDiagnosisRepository;
import com.solvd.hospital.services.impl.DiagnosisServiceImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDiagnosisRepositoryImpl implements PatientDiagnosisRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PATIENT_DIAGNOSIS_QUERY = "INSERT INTO patient_diagnosis (patient_id, diagnosis_id) " +
        "VALUES (?, ?)";
    private static final String FIND_ALL_BY_PATIENT_ID_QUERY = "SELECT * FROM patient_diagnosis WHERE patient_id = ?";
    private static final String DELETE_PATIENT_DIAGNOSIS_QUERY = "DELETE FROM patient_diagnosis WHERE patient_id = ? AND diagnosis_id = ?";

    private final DiagnosisServiceImpl diagnosisService = new DiagnosisServiceImpl();

    @Override
    public PatientDiagnosis create(PatientDiagnosis patientDiagnosis) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                 .prepareStatement(CREATE_PATIENT_DIAGNOSIS_QUERY)) {

            statement.setLong(1, patientDiagnosis.getPatientId());
            statement.setLong(2, patientDiagnosis.getDiagnosis().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient diagnosis failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating patient diagnosis", e);
        }
        return patientDiagnosis;
    }

    @Override
    public List<PatientDiagnosis> findAllByPatientId(long patientId) {
        List<PatientDiagnosis> patientDiagnoses = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    patientDiagnoses.add(resultSetToPatientDiagnosis(resultSet));
                }
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting patient diagnoses by patient ID", e);
        }
        return patientDiagnoses;
    }

    @Override
    public boolean delete(long patientId, long diagnosisId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PATIENT_DIAGNOSIS_QUERY)) {

            statement.setLong(1, patientId);
            statement.setLong(2, diagnosisId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient diagnosis", e);
        }
    }

    private PatientDiagnosis resultSetToPatientDiagnosis(ResultSet resultSet) throws SQLException, NotFoundException {
        return new PatientDiagnosis()
            .setPatientId(resultSet.getLong("patient_id"))
            .setDiagnosis(diagnosisService.findById(resultSet.getLong("diagnosis_id")));

    }
}

