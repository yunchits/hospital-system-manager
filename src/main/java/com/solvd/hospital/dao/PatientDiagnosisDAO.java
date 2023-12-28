package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.entities.PatientDiagnosis;

import java.util.List;
import java.util.Optional;

public interface PatientDiagnosisDAO {
    PatientDiagnosis create(PatientDiagnosis patientDiagnosis);

    List<PatientDiagnosis> findAll();

    List<PatientDiagnosis> findAllByPatientId(long patientId);

    Optional<PatientDiagnosis> findByPatientIdAndDiagnosisId(long patientId, long diagnosisId);

    PatientDiagnosis update(PatientDiagnosis patientDiagnosis, Diagnosis newDiagnosis);

    void delete(long patientId, long diagnosisId);
}
