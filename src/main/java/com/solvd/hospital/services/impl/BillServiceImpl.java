package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.dao.BillDAO;
import com.solvd.hospital.dao.impl.JDBCBillDAOImpl;
import com.solvd.hospital.services.BillService;

import java.time.LocalDate;
import java.util.List;

public class BillServiceImpl implements BillService {

    private final BillDAO dao;

    public BillServiceImpl() {
        this.dao = new JDBCBillDAOImpl();
    }

    @Override
    public Bill create(long patientId, double amount, LocalDate billingDate, PaymentStatus paymentStatus) {
        Bill bill = new Bill();

        bill.setPatientId(patientId);
        bill.setAmount(amount);
        bill.setBillingDate(billingDate);
        bill.setPaymentStatus(paymentStatus);

        return dao.create(bill);
    }

    @Override
    public List<Bill> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Bill> findByPatientIdAndPaymentStatus(long patientId, PaymentStatus status) throws EntityNotFoundException {
        List<Bill> bills = dao.findByPatientIdAndPaymentStatus(patientId, status);

        if (bills.isEmpty()) {
            throw new EntityNotFoundException("No " + status.name() + " Bills for Patient With ID: " + patientId);
        }

        return bills;
    }

    @Override
    public Bill findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Bill with ID: " + id + " not found")
        );
    }

    @Override
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

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Bill with ID: " + id + " not found");
        }
    }

}
