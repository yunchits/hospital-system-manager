package com.solvd.hospital.dao.jdbc.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.DataAccessException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.MedicationService;
import com.solvd.hospital.services.PatientService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPrescriptionDAOImpl implements PrescriptionDAO {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PRESCRIPTION_QUERY = "INSERT INTO prescriptions (doctor_id, patient_id, medication_id) " +
            "VALUES (?, ?, ?)";
    private static final String FIND_ALL_PRESCRIPTIONS_QUERY = "SELECT * FROM prescriptions";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM prescriptions WHERE id = ?";
    private static final String FIND_PRESCRIPTION_BY_PATIENT_ID_QUERY = "SELECT * FROM prescriptions WHERE patient_id = ?";
    private static final String UPDATE_PRESCRIPTION_QUERY = "UPDATE prescriptions " +
            "SET doctor_id = ?, patient_id = ?, medication_id = ? WHERE id = ?";
    private static final String DELETE_PRESCRIPTIONS_BY_PATIENT_ID_QUERY = "DELETE FROM prescriptions WHERE patient_id = ?";
    private static final String DELETE_PRESCRIPTIONS_BY_ID_QUERY = "DELETE FROM prescriptions WHERE id = ?";
    private static final String COUNT_PRESCRIPTIONS_BY_PATIENT_AND_MEDICATION =
            "SELECT COUNT(*) FROM prescriptions " +
                    "WHERE patient_id = ? AND medication_id = ?";

    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();
    private final MedicationService medicationService = new MedicationService();

    @Override
    public Prescription create(Prescription prescription) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(CREATE_PRESCRIPTION_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, prescription.getDoctor().getId());
            statement.setLong(2, prescription.getPatient().getId());
            statement.setLong(3, prescription.getMedication().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating prescription failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    prescription.setId(id);
                } else {
                    throw new SQLException("Creating prescription failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating prescription", e);
        }
        return prescription;
    }

    @Override
    public Optional<Prescription> findById(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToPrescription(resultSet));
                }
            }

        } catch (SQLException | EntityNotFoundException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Prescription> findAll() {
        List<Prescription> prescriptions = new ArrayList<>();

        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PRESCRIPTIONS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                prescriptions.add(resultSetToPrescription(resultSet));
            }

        } catch (SQLException | EntityNotFoundException e) {
            throw new DataAccessException("Error getting all doctors", e);
        }
        return prescriptions;
    }

    @Override
    public List<Prescription> findByPatientId(long patientId) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_PRESCRIPTION_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prescriptions.add(resultSetToPrescription(resultSet));
                }
            }
        } catch (SQLException | EntityNotFoundException e) {
            throw new DataAccessException(e);
        }
        return prescriptions;
    }

    @Override
    public Prescription update(Prescription prescription) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRESCRIPTION_QUERY)) {

            statement.setLong(1, prescription.getDoctor().getId());
            statement.setLong(2, prescription.getPatient().getId());
            statement.setLong(2, prescription.getMedication().getId());
            statement.setLong(2, prescription.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating prescription failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating prescription", e);
        }
        return prescription;
    }

    @Override
    public void deleteByPatientId(long patientId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRESCRIPTIONS_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRESCRIPTIONS_BY_ID_QUERY)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean isPrescriptionUnique(long patientId, long medicationId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_PRESCRIPTIONS_BY_PATIENT_AND_MEDICATION)) {

            statement.setLong(1, patientId);
            statement.setLong(2, medicationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking username uniqueness", e);
        }
        return false;
    }

    private Prescription resultSetToPrescription(ResultSet resultSet) throws SQLException, EntityNotFoundException {
        return new Prescription()
                .setId(resultSet.getLong("id"))
                .setPatient(patientService.findById(resultSet.getLong("patient_id")))
                .setDoctor(doctorService.findById(resultSet.getLong("doctor_id")))
                .setMedication(medicationService.findById(resultSet.getLong("medication_id")));
    }
}
