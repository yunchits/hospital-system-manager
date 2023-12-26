package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.PatientDiagnosisDAO;
import com.solvd.hospital.dao.impl.JDBCPatientDiagnosisDAOImpl;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.services.DiagnosisService;
import com.solvd.hospital.services.PatientDiagnosisService;

import java.util.List;

public class PatientDiagnosisServiceImpl implements PatientDiagnosisService {

    private final PatientDiagnosisDAO dao;

    private final DiagnosisService diagnosisService;

    public PatientDiagnosisServiceImpl() {
        this.dao = new JDBCPatientDiagnosisDAOImpl();
        this.diagnosisService = new DiagnosisServiceImpl();
    }

    @Override
    public PatientDiagnosis create(long patientId, long diagnosisId) throws RelatedEntityNotFound {
        Diagnosis diagnosis;
        try {
            diagnosis = diagnosisService.findById(diagnosisId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound("Diagnoses not found for ID: " + diagnosisId);
        }

        return dao.create(new PatientDiagnosis()
            .setPatientId(patientId)
            .setDiagnosis(diagnosis));
    }

    @Override
    public List<PatientDiagnosis> findAll() {
        return dao.findAll();
    }

    @Override
    public List<PatientDiagnosis> findByPatientId(long patientId) throws EntityNotFoundException {
        List<PatientDiagnosis> diagnoses = dao.findAllByPatientId(patientId);

        if (diagnoses.isEmpty()) {
            throw new EntityNotFoundException("Diagnoses not found for patient with ID: " + patientId);
        }

        return diagnoses;
    }

    @Override
    public PatientDiagnosis update(long patientId, long diagnosisId) throws RelatedEntityNotFound {
        Diagnosis diagnosis;
        try {
            diagnosis = diagnosisService.findById(diagnosisId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound("Diagnoses not found for ID: " + diagnosisId);
        }

        return dao.update(new PatientDiagnosis()
            .setPatientId(patientId)
            .setDiagnosis(diagnosis));
    }

    @Override
    public void delete(long patientId, long diagnosisId) throws EntityNotFoundException {
        if (dao.delete(patientId, diagnosisId)) {
            throw new EntityNotFoundException("Patient with this diagnosis not found");
        }
    }
}
