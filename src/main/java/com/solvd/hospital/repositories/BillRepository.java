package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository {
    Bill create(Bill bill);
    List<Bill> findByPatientId(long patientId);
    List<Bill> findByPatientIdAndBillingDate(long patientId, LocalDate date);
    List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status);
    Bill update (Bill bill);
    boolean delete(long id);
}
