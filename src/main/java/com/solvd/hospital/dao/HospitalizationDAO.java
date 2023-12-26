package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Hospitalization;

import java.util.List;
import java.util.Optional;

public interface HospitalizationDAO {
    Hospitalization create(Hospitalization hospitalization);
    List<Hospitalization> findAll();
    List<Hospitalization> findByPatientId(long patientId);
    Optional<Hospitalization> findById(long id);
    Hospitalization update(Hospitalization hospitalization);
    boolean delete (long id);
}
