package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.repositories.impl.PatientRepositoryImpl;
import com.solvd.hospital.services.PatientService;

public class PatientServiceImpl implements PatientService {

    private final PatientRepositoryImpl repository = new PatientRepositoryImpl();

    @Override
    public Patient findById(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException("Patient with id: " + id + " not found")
        );
    }
}
