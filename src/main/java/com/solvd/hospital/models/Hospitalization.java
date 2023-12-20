package com.solvd.hospital.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Hospitalization {
    private long id;
    private long patientId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
