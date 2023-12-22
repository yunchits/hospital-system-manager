package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Hospitalization;

import java.util.List;

public interface HospitalizationRepository {
    Hospitalization create(Hospitalization hospitalization);
    List<Hospitalization> findByPatientId(long patientId);
    boolean delete (long id);
}
