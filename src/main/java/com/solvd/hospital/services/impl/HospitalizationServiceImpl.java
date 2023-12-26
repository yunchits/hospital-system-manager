package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.HospitalizationDAO;
import com.solvd.hospital.dao.impl.JDBCHospitalizationDAOImpl;
import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.services.HospitalizationService;
import com.solvd.hospital.services.PatientService;

import java.time.LocalDate;
import java.util.List;

public class HospitalizationServiceImpl implements HospitalizationService {

    private final HospitalizationDAO dao;
    private final PatientService patientService;

    public HospitalizationServiceImpl() {
        this.dao = new JDBCHospitalizationDAOImpl();
        this.patientService = new PatientServiceImpl();
    }

    @Override
    public Hospitalization create(long patientId, LocalDate admissionDate, LocalDate dischargeDate) throws RelatedEntityNotFound {
        Patient patient;
        try {
            patient = patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound(e.getMessage());
        }

        return dao.create(new Hospitalization()
            .setPatient(patient)
            .setAdmissionDate(admissionDate)
            .setDischargeDate(dischargeDate));
    }

    @Override
    public List<Hospitalization> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Hospitalization> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Hospitalization> hospitalizations = dao.findByPatientId(patientId);

        if (hospitalizations.isEmpty()) {
            throw new EntityNotFoundException("Hospitalizations not found for patient with ID: " + patientId);
        }
        return hospitalizations;
    }

    @Override
    public Hospitalization findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Hospitalization with id: " + id + " not found")
        );
    }

    @Override
    public Hospitalization update(long id, long patientId, LocalDate admissionDate, LocalDate dischargeDate) throws RelatedEntityNotFound, EntityNotFoundException {
        findById(id);

        Patient patient;
        try {
            patient = patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound(e.getMessage());
        }

        return dao.create(new Hospitalization()
            .setId(id)
            .setPatient(patient)
            .setAdmissionDate(admissionDate)
            .setDischargeDate(dischargeDate));
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Hospitalization with id: " + id + " not found");
        }
    }
}
