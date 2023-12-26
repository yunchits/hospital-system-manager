package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.doctor.DoctorSalary;

import java.time.LocalDate;
import java.util.List;

public interface DoctorSalaryService {
    DoctorSalary create(double salary,
                        LocalDate paymentDate);

    List<DoctorSalary> findAll();

    DoctorSalary findById(long id) throws EntityNotFoundException;

    DoctorSalary update(long id,
                        double salary,
                        LocalDate paymentDate) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
