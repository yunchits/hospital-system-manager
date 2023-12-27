package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.dao.PatientDiagnosisDAO;
import com.solvd.hospital.services.DiagnosisService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPatientDiagnosisDAOImpl implements PatientDiagnosisDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PATIENT_DIAGNOSIS_QUERY = "INSERT INTO patients_diagnoses (patient_id, diagnosis_id) " +
        "VALUES (?, ?)";
    private static final String FIND_ALL_PATIENT_DIAGNOSIS_QUERY = "SELECT * FROM patients_diagnoses";
    private static final String FIND_ALL_BY_PATIENT_ID_QUERY = "SELECT * FROM patients_diagnoses WHERE patient_id = ?";
    private static final String FIND_BY_PATIENT_ID_AND_DIAGNOSIS_ID_QUERY = "SELECT * FROM patients_diagnoses " +
            "WHERE patient_id = ? AND diagnosis_id = ?";
    private static final String UPDATE_PATIENT_DIAGNOSIS_QUERY = "UPDATE patients_diagnoses " +
        "SET patient_id = ?, diagnosis_id = ? WHERE patient_id = ? AND diagnosis_id = ?";
    private static final String DELETE_PATIENT_DIAGNOSIS_QUERY = "DELETE FROM patients_diagnoses WHERE patient_id = ? AND diagnosis_id = ?";

    private final DiagnosisService diagnosisService = new DiagnosisService();

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
    public List<PatientDiagnosis> findAll() {
        List<PatientDiagnosis> patientDiagnoses = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PATIENT_DIAGNOSIS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                patientDiagnoses.add(resultSetToPatientDiagnosis(resultSet));
            }

        } catch (SQLException | EntityNotFoundException e) {
            throw new RuntimeException("Error getting all doctors", e);
        }
        return patientDiagnoses;
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
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting patient diagnoses by patient ID", e);
        }
        return patientDiagnoses;
    }

    @Override
    public Optional<PatientDiagnosis> findByPatientIdAndDiagnosisId(long patientId, long diagnosisId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_PATIENT_ID_AND_DIAGNOSIS_ID_QUERY)) {

            statement.setLong(1, patientId);
            statement.setLong(2, diagnosisId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToPatientDiagnosis(resultSet));
                }
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public PatientDiagnosis update(PatientDiagnosis patientDiagnosis, PatientDiagnosis newPatientDiagnosis) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PATIENT_DIAGNOSIS_QUERY)) {

            statement.setLong(1, newPatientDiagnosis.getPatientId());
            statement.setLong(2, newPatientDiagnosis.getDiagnosis().getId());
            statement.setLong(3, patientDiagnosis.getPatientId());
            statement.setLong(4, patientDiagnosis.getDiagnosis().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor", e);
        }
        return patientDiagnosis;
    }

    @Override
    public void delete(long patientId, long diagnosisId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PATIENT_DIAGNOSIS_QUERY)) {

            statement.setLong(1, patientId);
            statement.setLong(2, diagnosisId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient diagnosis", e);
        }
    }

    private PatientDiagnosis resultSetToPatientDiagnosis(ResultSet resultSet) throws SQLException, EntityNotFoundException {
        return new PatientDiagnosis()
            .setPatientId(resultSet.getLong("patient_id"))
            .setDiagnosis(diagnosisService.findById(resultSet.getLong("diagnosis_id")));

    }
}

