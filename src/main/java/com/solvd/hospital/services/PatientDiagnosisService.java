package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.PatientDiagnosisDAO;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.entities.PatientDiagnosis;

import java.util.List;

public class PatientDiagnosisService {

    private final PatientDiagnosisDAO dao;

    private final DiagnosisService diagnosisService;
    private final PatientService patientService;

    public PatientDiagnosisService() {
        this.dao = DAOFactory.createPatientDiagnosisDAO();
        this.diagnosisService = new DiagnosisService();
        this.patientService = new PatientService();
    }

    public PatientDiagnosis create(long patientId, long diagnosisId) throws RelatedEntityNotFound, EntityAlreadyExistsException {
        validatePatientDiagnosisDoesNotExist(patientId, diagnosisId);

        validateRelatedPatient(patientId);
        Diagnosis diagnosis = validateAndGetRelatedDiagnosis(diagnosisId);

        return dao.create(new PatientDiagnosis()
                .setPatientId(patientId)
                .setDiagnosis(diagnosis));
    }

    public List<PatientDiagnosis> findAll() {
        return dao.findAll();
    }

    public PatientDiagnosis findByPatientIdAndDiagnosisId(long patientId, long diagnosisId) throws EntityNotFoundException {
        return dao.findByPatientIdAndDiagnosisId(patientId, diagnosisId).orElseThrow(
                () -> new EntityNotFoundException("No patient with this diagnosis")
        );
    }

    public List<PatientDiagnosis> findByPatientId(long patientId) throws EntityNotFoundException, RelatedEntityNotFound {
        validateRelatedPatient(patientId);
        List<PatientDiagnosis> diagnoses = dao.findAllByPatientId(patientId);

        if (diagnoses.isEmpty()) {
            throw new EntityNotFoundException("Diagnoses not found for patient with ID: " + patientId);
        }

        return diagnoses;
    }

    public PatientDiagnosis update(long patientId, long diagnosisId, long newDiagnosisId) throws RelatedEntityNotFound, EntityAlreadyExistsException {
        validateRelatedPatient(patientId);

        Diagnosis diagnosis = validateAndGetRelatedDiagnosis(diagnosisId);
        Diagnosis newDiagnosis = validateAndGetRelatedDiagnosis(newDiagnosisId);

        PatientDiagnosis patientDiagnosis = new PatientDiagnosis()
                .setPatientId(patientId)
                .setDiagnosis(diagnosis);

        validatePatientDiagnosisDoesNotExist(patientId, newDiagnosisId);

        return dao.update(patientDiagnosis, newDiagnosis);
    }

    public void delete(long patientId, long diagnosisId) throws EntityNotFoundException, RelatedEntityNotFound {
        findByPatientId(patientId);
        dao.delete(patientId, diagnosisId);
    }

    private Diagnosis validateAndGetRelatedDiagnosis(long diagnosisId) throws RelatedEntityNotFound {
        Diagnosis diagnosis;
        try {
            diagnosis = diagnosisService.findById(diagnosisId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound("Diagnoses not found for ID: " + diagnosisId);
        }
        return diagnosis;
    }

    private void validateRelatedPatient(long patientId) throws RelatedEntityNotFound {
        try {
            patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound("Patient not found for ID " + patientId);
        }
    }

    private void validatePatientDiagnosisDoesNotExist(long patientId, long diagnosisId) throws EntityAlreadyExistsException {
        if (dao.findByPatientIdAndDiagnosisId(patientId, diagnosisId).isPresent()) {
            throw new EntityAlreadyExistsException("PatientDiagnosis already exists for patient ID: " + patientId +
                    " and diagnosis ID: " + diagnosisId);
        }
    }
}
