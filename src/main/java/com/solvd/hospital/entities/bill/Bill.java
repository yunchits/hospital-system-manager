package com.solvd.hospital.entities.bill;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Bill {
    private long id;
    private long patientId;
    private double amount;
    private LocalDate billingDate;
    private PaymentStatus paymentStatus;
}
