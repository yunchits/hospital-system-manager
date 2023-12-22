package com.solvd.hospital.entities;

import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Appointment {
    private long id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
}
