package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.doctor.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    Doctor create(Doctor doctor);
    List<Doctor> findAll();
    Optional<Doctor> findById(long id);
    Doctor update(Doctor doctor);
    boolean delete(long id);
}
