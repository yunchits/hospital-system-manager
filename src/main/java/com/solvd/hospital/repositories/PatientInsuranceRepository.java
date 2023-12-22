package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.patient.Insurance;

import java.util.Optional;

public interface PatientInsuranceRepository {
    Insurance create(Insurance insurance);
    Optional<Insurance> findById(long id);
    Insurance update(Insurance insurance);
    boolean delete(long id);
}
