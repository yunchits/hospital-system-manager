package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.doctor.DoctorSalary;

import java.util.Optional;

public interface DoctorSalaryRepository {
    DoctorSalary create(DoctorSalary salary);
    Optional<DoctorSalary> findById(long id);
    DoctorSalary update(DoctorSalary salary);
    boolean delete(long id);
}
