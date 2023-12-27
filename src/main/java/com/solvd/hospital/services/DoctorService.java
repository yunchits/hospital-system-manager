package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCDoctorDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisDoctorDAOImpl;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.doctor.DoctorSalary;

import java.time.LocalDate;
import java.util.List;

public class DoctorService {

    private final DoctorDAO dao;
    private final DoctorSalaryService salaryService;

    public DoctorService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisDoctorDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCDoctorDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
        this.salaryService = new DoctorSalaryService();
    }

    public Doctor create(String firstName, String lastName, String specialization, double salary, LocalDate paymentDate) {
        DoctorSalary doctorSalary = salaryService.create(salary, paymentDate);

        return dao.create(new Doctor()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setSpecialization(specialization)
            .setSalary(doctorSalary));
    }

    public List<Doctor> findAll() {
        return dao.findAll();
    }

    public Doctor findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Doctor with id: " + id + " not found")
        );
    }

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

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }
}
