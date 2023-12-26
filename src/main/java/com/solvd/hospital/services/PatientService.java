package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {

    Patient create(String firstName, String lastName, LocalDate birthDate, Gender gender);

    List<Patient> findAll();

    Patient findById(long id) throws EntityNotFoundException;

    Patient update(long id, String firstName, String lastName, LocalDate birthDate, Gender gender) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
