package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.ValidationUtils;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.dao.DiagnosisDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCDiagnosisDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisDiagnosisDAOImpl;
import com.solvd.hospital.entities.Diagnosis;

import java.util.List;

public class DiagnosisService {

    private final DiagnosisDAO dao;

    public DiagnosisService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisDiagnosisDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCDiagnosisDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }

    public Diagnosis create(String name, String description) throws HospitalException {
        ValidationUtils.validateStringLength(name, "name", 225);
        checkUniqueness(name);
        return dao.create(new Diagnosis()
                .setName(name)
                .setDescription(description));
    }

    public List<Diagnosis> findAll() {
        return dao.findAll();
    }

    public Diagnosis findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Diagnosis with id " + id + " is not found")
        );
    }

    public Diagnosis findByDiagnosesName(String name) throws EntityNotFoundException {
        return dao.findByDiagnosisName(name).orElseThrow(
                () -> new EntityNotFoundException("Diagnosis with name " + name + " is not found")
        );
    }

    public Diagnosis update(long id, String name, String description) throws HospitalException {
        ValidationUtils.validateStringLength(name, "name", 225);
        checkUniqueness(name);
        findById(id);
        return dao.update(new Diagnosis()
                .setId(id)
                .setName(name)
                .setDescription(description));
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

    private void checkUniqueness(String name) throws EntityAlreadyExistsException {
        if (!dao.isDiagnosisUnique(name)) {
            throw new EntityAlreadyExistsException("Diagnosis is not unique");
        }
    }
}
