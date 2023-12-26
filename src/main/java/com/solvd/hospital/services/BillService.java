package com.solvd.hospital.services;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public interface BillService {
    Bill create(long patientId,
                double amount,
                LocalDate billingDate,
                PaymentStatus paymentStatus);

    List<Bill> findAll();

    Bill update(long id,
                long patientId,
                double amount,
                LocalDate billingDate,
                PaymentStatus paymentStatus) throws EntityNotFoundException;

    void delete(long id) throws EntityNotFoundException;
}
