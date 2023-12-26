package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationDAO {
    Medication create(Medication medication);
    List<Medication> findAll();
    Optional<Medication> findById(long id);
    Medication update(Medication medication);
    boolean delete(long id);
}
