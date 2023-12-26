package com.solvd.hospital.dao;

import com.solvd.hospital.entities.doctor.DoctorSalary;

import java.util.List;
import java.util.Optional;

public interface DoctorSalaryDAO {
    DoctorSalary create(DoctorSalary salary);
    List<DoctorSalary> findAll();
    Optional<DoctorSalary> findById(long id);
    DoctorSalary update(DoctorSalary salary);
    boolean delete(long id);
}
