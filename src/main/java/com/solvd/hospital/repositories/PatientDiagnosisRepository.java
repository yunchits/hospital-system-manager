package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.PatientDiagnosis;

import java.util.List;

public interface PatientDiagnosisRepository {
    PatientDiagnosis create(PatientDiagnosis patientDiagnosis);

    List<PatientDiagnosis> findAllByPatientId(long patientId);

    boolean delete(long patientId, long diagnosisId);
}
