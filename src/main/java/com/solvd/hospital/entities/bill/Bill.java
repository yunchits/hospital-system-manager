package com.solvd.hospital.entities.bill;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Bill {
    private long id;
    private long patientId;
    private double amount;
    private LocalDate billingDate;
    private PaymentStatus paymentStatus;
}
