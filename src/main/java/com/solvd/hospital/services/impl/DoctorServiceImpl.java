package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.impl.JDBCDoctorDAOImpl;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.services.DoctorSalaryService;
import com.solvd.hospital.services.DoctorService;

import java.time.LocalDate;
import java.util.List;

public class DoctorServiceImpl implements DoctorService {

    private final JDBCDoctorDAOImpl dao;
    private final DoctorSalaryService salaryService;

    public DoctorServiceImpl() {
        this.dao = new JDBCDoctorDAOImpl();
        this.salaryService = new DoctorSalaryServiceImpl();
    }

    @Override
    public Doctor create(String firstName, String lastName, String specialization, double salary, LocalDate paymentDate) {
        DoctorSalary doctorSalary = salaryService.create(salary, paymentDate);

        return dao.create(new Doctor()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setSpecialization(specialization)
            .setSalary(doctorSalary));
    }

    @Override
    public List<Doctor> findAll() {
        return dao.findAll();
    }

    @Override
    public Doctor findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Doctor with id: " + id + " not found")
        );
    }

    @Override
    public Doctor update(long id,
                         String firstName,
                         String lastName,
                         String specialization,
                         double salary,
                         LocalDate paymentDate) throws EntityNotFoundException {
        Doctor doctor = findById(id);

        DoctorSalary doctorSalary = doctor.getSalary();
        if (doctorSalary == null) {
            doctorSalary = salaryService.create(salary, paymentDate);
        } else {
            doctorSalary.setSalary(salary).setPaymentDate(paymentDate);
            doctorSalary = salaryService.update(doctorSalary.getId(), salary, paymentDate);
        }

        DoctorSalary updatedSalary = salaryService.update(
            doctorSalary.getId(),
            doctorSalary.getSalary(),
            doctorSalary.getPaymentDate());

        return dao.update(new Doctor()
            .setId(id)
            .setFirstName(firstName)
            .setLastName(lastName)
            .setSpecialization(specialization)
            .setSalary(updatedSalary));
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Doctor with ID: " + id + " not found");
        }
    }
}
