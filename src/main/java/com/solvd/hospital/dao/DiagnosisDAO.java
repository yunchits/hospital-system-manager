package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Diagnosis;

import java.util.List;
import java.util.Optional;

public interface DiagnosisDAO {
    Diagnosis create(Diagnosis diagnosis);

    List<Diagnosis> findAll();

    Optional<Diagnosis> findById(long id);

    Optional<Diagnosis> findByDiagnosisName(String diagnosesName);

    Diagnosis update(Diagnosis diagnosis);

    void delete(long id);
}
