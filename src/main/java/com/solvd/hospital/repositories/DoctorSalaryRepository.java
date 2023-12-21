package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.doctor.DoctorSalary;

public interface DoctorSalaryRepository {
    DoctorSalary create(DoctorSalary salary);
    DoctorSalary getByDoctorId(long id);
    DoctorSalary update(DoctorSalary salary);
    boolean delete(long id);
}
