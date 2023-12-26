package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.DoctorSalaryDAO;
import com.solvd.hospital.dao.impl.JDBCDoctorSalaryDAOImpl;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.services.DoctorSalaryService;

import java.time.LocalDate;
import java.util.List;

public class DoctorSalaryServiceImpl implements DoctorSalaryService {

    private final DoctorSalaryDAO dao;

    public DoctorSalaryServiceImpl() {
        this.dao = new JDBCDoctorSalaryDAOImpl();
    }

    @Override
    public DoctorSalary create(double salary, LocalDate paymentDate) {
        return dao.create(new DoctorSalary()
            .setSalary(salary)
            .setPaymentDate(paymentDate));
    }

    @Override
    public List<DoctorSalary> findAll() {
        return dao.findAll();
    }

    @Override
    public DoctorSalary findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Doctor's salary with ID: " + id + " not found")
        );
    }

    @Override
    public DoctorSalary update(long id, double salary, LocalDate paymentDate) throws EntityNotFoundException {
        findById(id);
        return dao.update(new DoctorSalary()
            .setId(id)
            .setSalary(salary)
            .setPaymentDate(paymentDate));
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if(!dao.delete(id)) {
            throw new EntityNotFoundException("Doctor's salary with ID: " + id + " not found");
        }
    }
}
