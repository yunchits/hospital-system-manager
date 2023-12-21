package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Appointment;

import java.util.List;


public interface AppointmentRepository {
    Appointment create(Appointment appointment);
    List<Appointment> getByPatientId(long patientId);
    boolean delete(long id);
}
