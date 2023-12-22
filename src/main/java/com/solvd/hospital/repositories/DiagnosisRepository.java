package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Diagnosis;

import java.util.Optional;

public interface DiagnosisRepository {
    Diagnosis create(Diagnosis diagnosis);
    Optional<Diagnosis> findById(long id);
    Optional<Diagnosis> findByDiagnosisName(String diagnosesName);
    boolean delete (long id);
}
