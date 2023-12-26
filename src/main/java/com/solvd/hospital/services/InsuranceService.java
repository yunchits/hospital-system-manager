package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;

import java.time.LocalDate;
import java.util.List;

public interface InsuranceService {
    Insurance create(long id,
                     String policyNumber,
                     LocalDate expirationDate,
                     double coverageAmount,
                     InsuranceType type,
                     String insuranceProvider) throws DuplicateKeyException, RelatedEntityNotFound;

    List<Insurance> findAll();

    Insurance findById(long id) throws EntityNotFoundException;

    Insurance update(long id,
                     String policyNumber,
                     LocalDate expirationDate,
                     double coverageAmount,
                     InsuranceType type,
                     String insuranceProvider) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
