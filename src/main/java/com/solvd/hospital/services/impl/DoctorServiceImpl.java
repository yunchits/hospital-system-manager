package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.repositories.impl.DoctorRepositoryImpl;
import com.solvd.hospital.services.DoctorService;

public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepositoryImpl repository = new DoctorRepositoryImpl();


    @Override
    public Doctor findById(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException("Doctor with id: " + id + " not found")
        );
    }
}
