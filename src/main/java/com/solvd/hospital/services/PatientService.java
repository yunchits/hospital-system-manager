package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.patient.Patient;

public interface PatientService {
    Patient findById(long id) throws NotFoundException;
}
