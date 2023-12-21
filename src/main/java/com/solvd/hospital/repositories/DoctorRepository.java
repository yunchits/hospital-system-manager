package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.doctor.Doctor;

import java.util.List;

public interface DoctorRepository {
    Doctor create(Doctor doctor);
    List<Doctor> getAll();
    Doctor getById(long id);
    boolean delete(long id);
}
