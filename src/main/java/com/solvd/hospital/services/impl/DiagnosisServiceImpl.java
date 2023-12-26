package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.dao.impl.JDBCDiagnosisDAOImpl;
import com.solvd.hospital.services.DiagnosisService;

import java.util.List;

public class DiagnosisServiceImpl implements DiagnosisService {

    private final JDBCDiagnosisDAOImpl dao;

    public DiagnosisServiceImpl() {
        this.dao = new JDBCDiagnosisDAOImpl();
    }

    @Override
    public Diagnosis create(String name, String description) {
        return dao.create(new Diagnosis()
            .setName(name)
            .setDescription(description));
    }

    @Override
    public List<Diagnosis> findAll() {
        return dao.findAll();
    }

    @Override
    public Diagnosis findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Diagnosis with id " + id + " is not found")
        );
    }

    @Override
    public Diagnosis findByDiagnosesName(String name) throws EntityNotFoundException {
        return dao.findByDiagnosisName(name).orElseThrow(
            () -> new EntityNotFoundException("Diagnosis with name " + name + " is not found")
        );
    }

    @Override
    public Diagnosis update(long id, String name, String description) throws EntityNotFoundException {
        validateDiagnosisExists(id);

        return dao.update(new Diagnosis()
            .setId(id)
            .setName(name)
            .setDescription(description));
    }

    private void validateDiagnosisExists(long id) throws EntityNotFoundException {
        if (dao.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Diagnosis with ID: " + id + " not found");
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Diagnosis with ID: " + id + " not found");
        }
    }
}
