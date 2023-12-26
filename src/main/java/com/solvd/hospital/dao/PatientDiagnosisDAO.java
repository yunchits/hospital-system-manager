package com.solvd.hospital.dao;

import com.solvd.hospital.entities.PatientDiagnosis;

import java.util.List;

public interface PatientDiagnosisDAO {
    PatientDiagnosis create(PatientDiagnosis patientDiagnosis);
    List<PatientDiagnosis> findAll();
    List<PatientDiagnosis> findAllByPatientId(long patientId);
    PatientDiagnosis update(PatientDiagnosis patientDiagnosis);
    boolean delete(long patientId, long diagnosisId);
}
