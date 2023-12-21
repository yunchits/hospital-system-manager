package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.patient.Insurance;

public interface PatientInsuranceRepository {
    Insurance create(Insurance insurance);
    Insurance getByPatientId(long id);
    Insurance update(Insurance insurance);
    Insurance delete(long id);
}
