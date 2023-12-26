package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Diagnosis;

import java.util.List;

public interface DiagnosisService {
    Diagnosis create(String name, String description);

    List<Diagnosis> findAll();

    Diagnosis findById(long id) throws EntityNotFoundException;

    Diagnosis findByDiagnosesName(String name) throws EntityNotFoundException;

    Diagnosis update(long id, String name, String description) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
