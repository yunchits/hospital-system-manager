package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.entities.Hospitalization;

import java.time.LocalDate;
import java.util.List;

public interface HospitalizationService {

    Hospitalization create(long patientId,
                           LocalDate admissionDate,
                           LocalDate dischargeDate) throws EntityNotFoundException, RelatedEntityNotFound;

    List<Hospitalization> findAll();

    List<Hospitalization> findByPatientId(long patientId) throws EntityNotFoundException;

    Hospitalization findById(long id) throws EntityNotFoundException;

    Hospitalization update(long id,
                           long patientId,
                           LocalDate admissionDate,
                           LocalDate dischargeDate) throws EntityNotFoundException, RelatedEntityNotFound;

    void delete(long id) throws EntityNotFoundException;
}
