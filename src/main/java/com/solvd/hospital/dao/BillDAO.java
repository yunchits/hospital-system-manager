package com.solvd.hospital.dao;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillDAO {
    Bill create(Bill bill);
    List<Bill> findAll();
    Optional<Bill> findById(long id);
    List<Bill> findByPatientId(long patientId);
    List<Bill> findByPatientIdAndBillingDate(long patientId, LocalDate date);
    List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status);
    Bill update (Bill bill);
    boolean delete(long id);
}
