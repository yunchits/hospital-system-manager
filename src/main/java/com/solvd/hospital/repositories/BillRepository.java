package com.solvd.hospital.repositories;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.util.List;

public interface BillRepository {
    Bill create(Bill bill);
    List<Bill> getByPatientId(long patientId);
    List<Bill> getByPatientIdAndPaymentStatus(long patientId, PaymentStatus status);
    Bill update (Bill bill);
    boolean delete(long id);
}
