package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Medication;

import java.util.List;

public interface MedicationRepository {
    Medication create(Medication medication);
    List<Medication> getAll();
    Medication update(Medication medication);
    boolean delete(long id);
}
