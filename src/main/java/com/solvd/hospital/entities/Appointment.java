package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Appointment {
    private long id;
    private long patientId;
    private long doctorId;
    private LocalDateTime appointmentDateTime;
}
