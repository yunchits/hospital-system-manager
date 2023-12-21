package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Hospitalization {
    private long id;
    private long patientId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
