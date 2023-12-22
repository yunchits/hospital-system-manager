package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationRepository {
    Medication create(Medication medication);
    List<Medication> findAll();
    Optional<Medication> findById(long id);
    Medication update(Medication medication);
    boolean delete(long id);
}
