package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.dao.impl.JDBCPatientDAOImpl;
import com.solvd.hospital.services.PatientService;

import java.time.LocalDate;
import java.util.List;

public class PatientServiceImpl implements PatientService {

    private final JDBCPatientDAOImpl dao;

    public PatientServiceImpl() {
        this.dao = new JDBCPatientDAOImpl();
    }

    @Override
    public Patient create(String firstName, String lastName, LocalDate birthDate, Gender gender) {
        return dao.create(new Patient()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setBirthDate(birthDate)
            .setGender(gender));
    }

    @Override
    public List<Patient> findAll() {
        return dao.findAll();
    }

    @Override
    public Patient findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Patient with id: " + id + " not found")
        );
    }

    @Override
    public Patient update(long id, String firstName, String lastName, LocalDate birthDate, Gender gender) throws EntityNotFoundException {
        Patient patient = new Patient();

        validatePatientExists(id);

        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);

        return dao.create(patient);
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Patient with id: " + id + " not found");
        }
    }

    private void validatePatientExists(long id) throws EntityNotFoundException {
        findById(id);
    }
}
