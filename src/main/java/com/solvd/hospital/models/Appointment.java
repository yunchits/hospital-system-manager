package com.solvd.hospital.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Appointment {
    private long id;
    private long patientId;
    private long doctorId;
    private LocalDate appointmentDatetime;
}
