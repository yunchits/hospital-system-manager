package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.repositories.impl.DiagnosisRepositoryImpl;
import com.solvd.hospital.services.DiagnosisService;

public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepositoryImpl repository = new DiagnosisRepositoryImpl();

    @Override
    public Diagnosis findById(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException("Diagnosis with id " + id + " is not found")
        );
    }

    @Override
    public Diagnosis findByDiagnosesName(String name) throws NotFoundException {
        return repository.findByDiagnosisName(name).orElseThrow(
            () -> new NotFoundException("Diagnosis with name " + name + " is not found")
        );
    }
}
