package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.MedicationDAO;
import com.solvd.hospital.dao.impl.JDBCMedicationDAOImpl;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.services.MedicationService;

import java.util.List;

public class MedicationServiceImpl implements MedicationService {

    private final MedicationDAO dao;

    public MedicationServiceImpl() {
        this.dao = new JDBCMedicationDAOImpl();
    }

    @Override
    public Medication create(String name, String description) {
        return dao.create(new Medication()
            .setName(name)
            .setDescription(description));
    }

    @Override
    public List<Medication> findAll() {
        return dao.findAll();
    }

    @Override
    public Medication findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Medication with id " + id + " not found")
        );
    }

    @Override
    public Medication update(long id, String name, String description) throws EntityNotFoundException {
        validateMedicationExists(id);
        return dao.create(new Medication()
            .setId(id)
            .setName(name)
            .setDescription(description));
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Medication with ID: " + id + " not found");
        }
    }

    private void validateMedicationExists(long id) throws EntityNotFoundException {
        if (dao.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Medication with ID: " + id + " doesn't exist");
        }
    }
}
