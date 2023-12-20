package com.solvd.hospital.models;

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
