package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.NotFoundException;
import com.solvd.hospital.entities.Medication;

public interface MedicationService {
    Medication findById(long id) throws NotFoundException;
}
