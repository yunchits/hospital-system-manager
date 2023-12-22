package com.solvd.hospital.repositories.impl;

import com.solvd.hospital.common.database.ConnectionPool;
import com.solvd.hospital.common.database.ReusableConnection;
import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.repositories.PrescriptionRepository;
import com.solvd.hospital.services.impl.DoctorServiceImpl;
import com.solvd.hospital.services.impl.MedicationServiceImpl;
import com.solvd.hospital.services.impl.PatientServiceImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String CREATE_PRESCRIPTION_QUERY = "INSERT INTO prescriptions (doctor_id, patient_id, medication_id) " +
        "VALUES (?, ?, ?)";
    private static final String GET_PRESCRIPTION_BY_PATIENT_ID_QUERY = "SELECT * FROM prescriptions WHERE patient_id = ?";
    private static final String DELETE_PRESCRIPTIONS_BY_PATIENT_ID_QUERY = "DELETE FROM prescriptions WHERE patient_id = ?";

    private final PatientServiceImpl patientService = new PatientServiceImpl();
    private final DoctorServiceImpl doctorService = new DoctorServiceImpl();
    private final MedicationServiceImpl medicationService = new MedicationServiceImpl();

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
            throw new RuntimeException("Error creating prescription", e);
        }
        return prescription;
    }

    @Override
    public List<Prescription> findByPatientId(long patientId) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRESCRIPTION_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prescriptions.add(resultSetToPrescription(resultSet));
                }
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prescriptions;
    }

    @Override
    public boolean deleteByPatientId(long patientId) {
        try (ReusableConnection connection = POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRESCRIPTIONS_BY_PATIENT_ID_QUERY)) {

            statement.setLong(1, patientId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Prescription resultSetToPrescription(ResultSet resultSet) throws SQLException, NotFoundException {
        return new Prescription()
            .setId(resultSet.getLong("id"))
            .setPatient(patientService.findById(resultSet.getLong("patient_id")))
            .setDoctor(doctorService.findById(resultSet.getLong("doctor_id")))
            .setMedication(medicationService.findById(resultSet.getLong("medication_id")));
    }
}
