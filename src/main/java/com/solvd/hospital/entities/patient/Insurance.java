package com.solvd.hospital.entities.patient;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class Insurance {
    private long patientId;
    private String policyNumber;
    private LocalDate expirationDate;
    private double coverageAmount;
    private InsuranceType type;
    private String insuranceProvider;

    @Override
    public String toString() {
        return String.format(
            "%n[%d] Policy Number: %s, Expiration Date: %s, Coverage Amount: %.2f, Type: %s, Provider: %s",
            patientId, policyNumber, expirationDate, coverageAmount, type, insuranceProvider
        );
    }
}
