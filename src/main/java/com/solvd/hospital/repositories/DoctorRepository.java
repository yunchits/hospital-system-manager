package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.doctor.Doctor;

public interface DoctorRepository {
    Doctor create(Doctor doctor);
    Doctor getById(long id);
    boolean delete(long id);
}
