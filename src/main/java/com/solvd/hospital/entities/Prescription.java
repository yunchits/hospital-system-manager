package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prescription {
    private long id;
    private Long doctorId;
    private long patientId;
    private long medicationId;
}
