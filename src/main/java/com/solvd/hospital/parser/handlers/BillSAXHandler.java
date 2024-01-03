package com.solvd.hospital.parser.handlers;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillSAXHandler extends DefaultHandler {

    @Getter
    private List<Bill> bills;
    private Bill currentBill;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;

        if ("bill".equals(currentElement)) {
            currentBill = new Bill();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty() && currentBill != null) {
            if ("patientId".equals(currentElement)) {
                currentBill.setPatientId(Long.parseLong(value));
            } else if ("amount".equals(currentElement)) {
                currentBill.setAmount(Double.parseDouble(value));
            } else if ("billingDate".equals(currentElement)) {
                currentBill.setBillingDate(LocalDate.parse(value));
            } else if ("paymentStatus".equals(currentElement)) {
                currentBill.setPaymentStatus(PaymentStatus.valueOf(value));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("bill".equals(qName)) {
            if (bills == null) {
                bills = new ArrayList<>();
            }
            bills.add(currentBill);
            currentBill = null;
        }
    }
}
