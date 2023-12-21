package com.solvd.hospital.entities.patient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Insurance {
    private long patientId;
    private String policyNumber;
    private LocalDate expirationDate;
    private double coverageAmount;
    private InsuranceType type;
    private String insuranceProvider;
}
