package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Diagnosis;

public interface DiagnosisService {
    Diagnosis findById(long id) throws NotFoundException;

    Diagnosis findByDiagnosesName(String name) throws NotFoundException;
}
