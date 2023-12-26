package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment create(long patientId, long doctorId, LocalDateTime appointmentDateTime) throws EntityNotFoundException;

    List<Appointment> findAll();

    Appointment findById(long id) throws EntityNotFoundException;

    List<Appointment> findByPatientId(long patientId) throws EntityNotFoundException;

    List<Appointment> findByDoctorId(long doctorId) throws EntityNotFoundException;

    Appointment update(long id, long patientId, long doctorId, LocalDateTime appointmentDateTime) throws EntityNotFoundException, RelatedEntityNotFound;

    void delete(long id) throws EntityNotFoundException;
}
