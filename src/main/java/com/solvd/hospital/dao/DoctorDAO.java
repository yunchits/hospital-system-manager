package com.solvd.hospital.dao;

import com.solvd.hospital.entities.doctor.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDAO {
    Doctor create(Doctor doctor);

    List<Doctor> findAll();

    Optional<Doctor> findById(long id);

    Optional<Doctor> findByUserId(long id);

    Doctor update(Doctor doctor);

    void delete(long id);
}
