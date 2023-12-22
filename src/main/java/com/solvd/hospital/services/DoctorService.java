package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.doctor.Doctor;

public interface DoctorService {
    Doctor findById(long id) throws NotFoundException;
}
