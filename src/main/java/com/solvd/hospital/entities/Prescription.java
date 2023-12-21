package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Prescription {
    private long id;
    private Long doctorId;
    private long patientId;
    private long medicationId;
}
