package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.doctor.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    Doctor create(String firstName,
                  String lastName,
                  String specialization,
                  double salary,
                  LocalDate paymentDate);

    List<Doctor> findAll();

    Doctor findById(long id) throws EntityNotFoundException;

    Doctor update(long id,
                  String firstName,
                  String lastName,
                  String specialization,
                  double salary,
                  LocalDate paymentDate) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
