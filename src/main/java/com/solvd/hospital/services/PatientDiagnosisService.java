package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.entities.PatientDiagnosis;

import java.util.List;

public interface PatientDiagnosisService {

    PatientDiagnosis create(long patientId, long diagnosisId) throws RelatedEntityNotFound;

    List<PatientDiagnosis> findAll();

    List<PatientDiagnosis> findByPatientId(long patientId) throws EntityNotFoundException;

    PatientDiagnosis update(long patientId, long diagnosisId) throws RelatedEntityNotFound;

    void delete(long patientId, long diagnosisId) throws EntityNotFoundException;
}
