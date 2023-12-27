package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.MedicationDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCMedicationDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisMedicationDAOImpl;
import com.solvd.hospital.entities.Medication;

import java.util.List;

public class MedicationService {

    private final MedicationDAO dao;

    public MedicationService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisMedicationDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCMedicationDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }

    public Medication create(String name, String description) {
        return dao.create(new Medication()
                .setName(name)
                .setDescription(description));
    }

    public List<Medication> findAll() {
        return dao.findAll();
    }

    public Medication findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Medication with id " + id + " not found")
        );
    }

    public Medication update(long id, String name, String description) throws EntityNotFoundException {
        findById(id);
        return dao.update(new Medication()
                .setId(id)
                .setName(name)
                .setDescription(description));
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }
}
