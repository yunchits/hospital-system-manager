package com.solvd.hospital.dao;

import com.solvd.hospital.entities.patient.Insurance;

import java.util.List;
import java.util.Optional;

public interface InsuranceDAO {
    Insurance create(Insurance insurance);

    List<Insurance> findAll();

    Optional<Insurance> findById(long id);

    Insurance update(Insurance insurance);

    void delete(long id);
}
