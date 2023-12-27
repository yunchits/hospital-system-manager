package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.DoctorSalaryDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCDoctorSalaryDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisDoctorSalaryDAOImpl;
import com.solvd.hospital.entities.doctor.DoctorSalary;

import java.time.LocalDate;
import java.util.List;

public class DoctorSalaryService {

    private final DoctorSalaryDAO dao;

    public DoctorSalaryService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisDoctorSalaryDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCDoctorSalaryDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }

    public DoctorSalary create(double salary, LocalDate paymentDate) {
        return dao.create(new DoctorSalary()
                .setSalary(salary)
                .setPaymentDate(paymentDate));
    }

    public List<DoctorSalary> findAll() {
        return dao.findAll();
    }

    public DoctorSalary findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doctor's salary with ID: " + id + " not found")
        );
    }

    public DoctorSalary update(long id, double salary, LocalDate paymentDate) throws EntityNotFoundException {
        findById(id);
        return dao.update(new DoctorSalary()
                .setId(id)
                .setSalary(salary)
                .setPaymentDate(paymentDate));
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }
}
