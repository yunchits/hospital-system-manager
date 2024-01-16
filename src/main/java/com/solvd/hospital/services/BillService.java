package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.dao.BillDAO;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public class BillService {

    private final BillDAO dao;

    public BillService() {
        this.dao = DAOFactory.createBillDAO();
    }
    
    public Bill create(long patientId, double amount, LocalDate billingDate, PaymentStatus paymentStatus) {
        Bill bill = new Bill();

        bill.setPatientId(patientId);
        bill.setAmount(amount);
        bill.setBillingDate(billingDate);
        bill.setPaymentStatus(paymentStatus);

        return dao.create(bill);
    }

    public List<Bill> findAll() {
        return dao.findAll();
    }
    
    public List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status) throws EntityNotFoundException {
        List<Bill> bills = dao.findByPatientIdAndPaymentStatus(patientId, status);

        if (bills.isEmpty()) {
            throw new EntityNotFoundException("No " + status.name() + " Bills for Patient With ID: " + patientId);
        }

        return bills;
    }
    
    public Bill findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Bill with ID: " + id + " not found")
        );
    }

    public Bill update(long id, long patientId, double amount, LocalDate billingDate, PaymentStatus paymentStatus) throws EntityNotFoundException {
        Bill bill = new Bill();

        findById(id);

        bill.setId(id);
        bill.setPatientId(patientId);
        bill.setAmount(amount);
        bill.setBillingDate(billingDate);
        bill.setPaymentStatus(paymentStatus);

        return dao.update(bill);
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

}
