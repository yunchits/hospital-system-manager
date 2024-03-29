package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.ValidationUtils;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.dao.MedicationDAO;
import com.solvd.hospital.entities.Medication;

import java.util.List;

public class MedicationService {

    private final MedicationDAO dao;

    public MedicationService() {
        this.dao = DAOFactory.createMedicationDAO();
    }

    public Medication create(String name, String description) throws HospitalException {
        ValidationUtils.validateStringLength(name, "name", 225);
        checkUniqueness(name);
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

    public Medication update(long id, String name, String description) throws HospitalException {
        ValidationUtils.validateStringLength(name, "name", 225);
        checkUniqueness(name);
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

    private void checkUniqueness(String name) throws EntityAlreadyExistsException {
        if (!dao.isMedicationUnique(name)) {
            throw new EntityAlreadyExistsException("Medication is not unique");
        }
    }
}
