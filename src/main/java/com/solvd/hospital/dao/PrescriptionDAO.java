package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionDAO {
    Prescription create(Prescription prescription);

    Optional<Prescription> findById(long id);

    List<Prescription> findAll();

    List<Prescription> findByPatientId(long patientId);

    Prescription update(Prescription prescription);

    void deleteByPatientId(long patientId);

    void delete(long id);

    boolean isPrescriptionUnique(long patientId, long medicationId);
}
