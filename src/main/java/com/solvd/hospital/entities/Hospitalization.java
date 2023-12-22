package com.solvd.hospital.entities;

import com.solvd.hospital.entities.patient.Patient;
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
    private Patient patient;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
