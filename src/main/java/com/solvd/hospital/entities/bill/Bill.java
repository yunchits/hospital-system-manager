package com.solvd.hospital.entities.bill;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class Bill {
    private long id;
    private long patientId;
    private double amount;
    private LocalDate billingDate;
    private PaymentStatus paymentStatus;

    @Override
    public String toString() {
        return String.format(
            "%nBill [%d] - Patient ID: %d, Amount: %.2f, Billing Date: %s, Payment Status: %s",
            id, patientId, amount, billingDate, paymentStatus
        );
    }
}
