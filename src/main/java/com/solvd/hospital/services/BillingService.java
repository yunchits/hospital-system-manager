package com.solvd.hospital.services;

import com.solvd.hospital.entities.BillingOperation;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.time.LocalDate;

public class BillingService {

    private final BillService billService;
    private double billingAmount;

    public BillingService() {
        this.billService = new BillService();
        this.billingAmount = 0;
    }

    public void performOperation(BillingOperation operation) {
        this.billingAmount += operation.getCost();
    }

    public Bill createBill(long patientId) {
        Bill bill = billService.create(
                patientId,
                billingAmount,
                LocalDate.now(),
                PaymentStatus.UNPAID
        );
        resetBillingAmount();
        return bill;
    }

    private void resetBillingAmount() {
        this.billingAmount = 0;
    }
}
