package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.HospitalizationDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCHospitalizationDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisHospitalizationDAOImpl;
import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.entities.patient.Patient;

import java.time.LocalDate;
import java.util.List;

public class HospitalizationService {

    private final HospitalizationDAO dao;
    private final PatientService patientService;

    public HospitalizationService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisHospitalizationDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCHospitalizationDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
        this.patientService = new PatientService();
    }

    public Hospitalization create(long patientId, LocalDate admissionDate, LocalDate dischargeDate) throws RelatedEntityNotFound, InvalidArgumentException {
        validateDate(admissionDate, dischargeDate);

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

    public List<Hospitalization> findAll() {
        return dao.findAll();
    }

    public List<Hospitalization> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Hospitalization> hospitalizations = dao.findByPatientId(patientId);

        if (hospitalizations.isEmpty()) {
            throw new EntityNotFoundException("Hospitalizations not found for patient with ID: " + patientId);
        }
        return hospitalizations;
    }

    public Hospitalization findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Hospitalization with id: " + id + " not found")
        );
    }

    public Hospitalization update(long id, long patientId, LocalDate admissionDate, LocalDate dischargeDate) throws RelatedEntityNotFound, EntityNotFoundException, InvalidArgumentException {
        validateDate(admissionDate, dischargeDate);

        findById(id);

        Patient patient;
        try {
            patient = patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound(e.getMessage());
        }

        return dao.update(new Hospitalization()
            .setId(id)
            .setPatient(patient)
            .setAdmissionDate(admissionDate)
            .setDischargeDate(dischargeDate));
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

    private static void validateDate(LocalDate admissionDate, LocalDate dischargeDate) throws InvalidArgumentException {
        if (admissionDate.isAfter(LocalDate.now()) && dischargeDate.isBefore(admissionDate)) {
            throw new InvalidArgumentException("Admission date and time must be in the future " +
                "and discharge date must be after admission date");
        }
    }
}
