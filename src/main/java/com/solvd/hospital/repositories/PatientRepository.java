package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.patient.Patient;

import java.util.List;

public interface PatientRepository {
    Patient create(Patient patient);
    List<Patient> getAll();
    Patient getById(long id);
    boolean delete(long id);
}
