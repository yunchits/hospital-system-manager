package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Medication;

import java.util.List;

public interface MedicationService {
    Medication create(String name, String description);

    List<Medication> findAll();

    Medication findById(long id) throws EntityNotFoundException;

    Medication update(long id, String name, String description) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
