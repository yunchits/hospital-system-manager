package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Prescription;

import java.util.List;

public interface PrescriptionRepository {
    Prescription create(Prescription prescription);
    List<Prescription> getByPatientId(long patientId);
    boolean deleteAllByPatientId(long patientId);
}
