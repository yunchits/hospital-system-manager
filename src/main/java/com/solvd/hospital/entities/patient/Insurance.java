package com.solvd.hospital.entities.patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Insurance {
    private long patientId;
    private String policyNumber;
    private LocalDate expirationDate;
    private double coverageAmount;
    private InsuranceType type;
    private String insuranceProvider;
}
