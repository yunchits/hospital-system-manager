package com.solvd.hospital.entities;

import lombok.Getter;

@Getter
public enum BillingOperation {
    DIAGNOSIS(50.0),
    PRESCRIPTION(25.0),
    HOSPITALIZATION(100.0);

    private final double cost;

    BillingOperation(double cost) {
        this.cost = cost;
    }

}
