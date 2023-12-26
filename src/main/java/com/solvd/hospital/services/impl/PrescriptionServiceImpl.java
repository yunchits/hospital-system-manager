package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.dao.impl.JDBCPrescriptionDAOImpl;
import com.solvd.hospital.services.PrescriptionService;

import java.util.List;

public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionDAO dao;

    public PrescriptionServiceImpl() {
        this.dao = new JDBCPrescriptionDAOImpl();
    }

    @Override
    public Prescription create(Doctor doctor, Patient patient, Medication medication) {
        return dao.create(new Prescription()
            .setDoctor(doctor)
            .setPatient(patient)
            .setMedication(medication));
    }

    @Override
    public List<Prescription> findAll() {
        return dao.findAll();
    }

    @Override
    public Prescription findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Prescription with ID: " + id + " not found")
        );
    }

    @Override
    public List<Prescription> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Prescription> prescriptions = dao.findByPatientId(patientId);

        if (prescriptions.isEmpty()) {
            throw new EntityNotFoundException("Prescriptions not found for patient with ID: " + patientId);
        }

        return prescriptions;
    }

    @Override
    public Prescription update(long id, Doctor doctor, Patient patient, Medication medication) throws DuplicateKeyException {
        validatePrescriptionExists(id);
        return dao.update(new Prescription()
            .setId(id)
            .setDoctor(doctor)
            .setPatient(patient)
            .setMedication(medication));
    }

    private void validatePrescriptionExists(long id) throws DuplicateKeyException {
        if (dao.findById(id).isEmpty()) {
            throw new DuplicateKeyException("Prescription with ID: " + id + " already exists");
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Prescription with ID: " + id + " not found");
        }
    }
}
