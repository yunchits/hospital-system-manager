package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCPrescriptionDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisPrescriptionDAOImpl;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;

import java.util.List;

public class PrescriptionService {
    private final PrescriptionDAO dao;

    public PrescriptionService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisPrescriptionDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCPrescriptionDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }

    public Prescription create(Doctor doctor, Patient patient, Medication medication) {
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

    public Prescription update(long id, Doctor doctor, Patient patient, Medication medication) throws EntityNotFoundException {
        findById(id);
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
}
