package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.patient.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient create(Patient patient);
    List<Patient> findAll();
    Optional<Patient> findById(long id);
    boolean delete(long id);
}
