package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.repositories.impl.MedicationRepositoryImpl;
import com.solvd.hospital.services.MedicationService;

public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepositoryImpl repository = new MedicationRepositoryImpl();

    @Override
    public Medication findById(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException("Medication with id " + id + " not found")
        );
    }
}
