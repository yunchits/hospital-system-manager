package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Diagnosis;

import java.util.List;

public interface PatientDiagnosisRepository {
    Diagnosis create(Diagnosis diagnosis);
    List<Diagnosis> getAllByPatientId(long patientId);
    boolean delete (long patientId, String diagnosisName);
}
