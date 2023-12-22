package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository {
    Appointment create(Appointment appointment);
    List<Appointment> findByPatientId(long patientId);
    boolean deleteByPatient(long patientId);
}
