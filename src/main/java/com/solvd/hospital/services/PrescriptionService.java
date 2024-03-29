package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.patient.Patient;

import java.util.List;

public class PrescriptionService {
    private final PrescriptionDAO dao;

    public PrescriptionService() {
        this.dao = DAOFactory.createPrescriptionDAO();
    }

    public Prescription create(Doctor doctor, Patient patient, Medication medication)
            throws EntityAlreadyExistsException {
        checkPrescriptionUniqueness(patient, medication);
        return dao.create(new Prescription()
                .setDoctor(doctor)
                .setPatient(patient)
                .setMedication(medication));
    }

    public List<Prescription> findAll() {
        return dao.findAll();
    }

    public Prescription findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Prescription with ID: " + id + " not found")
        );
    }

    public List<Prescription> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Prescription> prescriptions = dao.findByPatientId(patientId);

        if (prescriptions.isEmpty()) {
            throw new EntityNotFoundException("Prescriptions not found for patient with ID: " + patientId);
        }

        return prescriptions;
    }

    public Prescription update(long id, Doctor doctor, Patient patient, Medication medication) throws EntityNotFoundException, EntityAlreadyExistsException {
        findById(id);
        checkPrescriptionUniqueness(patient, medication);
        return dao.update(new Prescription()
                .setId(id)
                .setDoctor(doctor)
                .setPatient(patient)
                .setMedication(medication));
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

    private void checkPrescriptionUniqueness(Patient patient, Medication medication) throws EntityAlreadyExistsException {
        if (dao.isPrescriptionUnique(patient.getId(), medication.getId())) {
            throw new EntityAlreadyExistsException("Patient has already been prescribed this medication");
        }
    }
}
