package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDAO {
    Doctor createWithUser(Doctor doctor);

    Doctor create(Doctor doctor);

    List<Doctor> findAll();

    Optional<Doctor> findById(long id);

    Optional<Doctor> findByUserId(long id);

    Doctor update(Doctor doctor);

    Doctor updateUserId(Doctor doctor);

    void delete(long id);
}
