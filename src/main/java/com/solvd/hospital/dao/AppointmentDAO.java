package com.solvd.hospital.dao;

import com.solvd.hospital.entities.Appointment;

import java.util.List;
import java.util.Optional;


public interface AppointmentDAO {
    Appointment create(Appointment appointment);

    List<Appointment> findAll();

    Optional<Appointment> findById(long id);

    List<Appointment> findByPatientId(long patientId);

    List<Appointment> findByDoctorId(long doctorId);

    Appointment update(Appointment appointment);

    void delete(long id);

    void deleteByPatient(long patientId);
}
