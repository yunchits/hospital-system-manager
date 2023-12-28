package com.solvd.hospital.dao;

import com.solvd.hospital.entities.patient.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    Patient create(Patient patient);

    List<Patient> findAll();

    Optional<Patient> findById(long id);

    Optional<Patient> findByUserId(long userId);

    Patient update(Patient patient);

    void delete(long id);
}
