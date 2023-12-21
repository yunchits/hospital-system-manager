package com.solvd.hospital.entities;

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
    private long patientId; //todo Patient patient
    private long doctorId;
    private LocalDateTime appointmentDateTime;
}
