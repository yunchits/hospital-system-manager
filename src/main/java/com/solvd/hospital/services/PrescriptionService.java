package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;

import java.util.List;

public interface PrescriptionService {

    Prescription create(Doctor doctor,
                        Patient patient,
                        Medication medication);

    List<Prescription> findAll();

    List<Prescription> findByPatientId(long patientId) throws EntityNotFoundException;

    Prescription findById(long id) throws EntityNotFoundException;

    Prescription update(long id,
                        Doctor doctor,
                        Patient patient,
                        Medication medication) throws DuplicateKeyException;

    void delete(long id) throws EntityNotFoundException;
}
