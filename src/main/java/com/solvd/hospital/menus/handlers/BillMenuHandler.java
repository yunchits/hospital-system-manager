package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.sax.parser.HospitalSAXParser;
import com.solvd.hospital.sax.parser.handlers.BillSAXHandler;
import com.solvd.hospital.services.BillService;
import com.solvd.hospital.services.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class BillMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(BillMenuHandler.class);

    private final InputScanner scanner;

    private final BillService billService;

    public BillMenuHandler() {
        this.scanner = new InputScanner();
        this.billService = new BillService();
    }

    @Override
    public void display() {
        printBills();

        int choice;
        do {
            LOGGER.info("Bill Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createBill();
                    break;
                case 2:
                    printBills();
                    break;
                case 3:
                    updateBill();
                    break;
                case 4:
                    deleteBill();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createBill() {
        LOGGER.info("Choose source for bill creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (SAX)");
        int choice = scanner.scanInt(1, 2);

        if (choice == 1) {
            createBillFromConsole();
        } else if (choice == 2) {
            createBillFromXML();
        }
    }

    private void createBillFromConsole() {
        long patientId = getPatientId();

        double amount = getAmount();

        LocalDate billingDate = getLocalDate();

        PaymentStatus status = selectPaymentStatus();

        billService.create(patientId, amount, billingDate, status);
    }

    private LocalDate getLocalDate() {
        LOGGER.info("Enter billing date:");
        return scanner.scanLocalDate();
    }

    private double getAmount() {
        LOGGER.info("Enter billing amount:");
        return scanner.scanPositiveDouble();
    }

    private long getPatientId() {
        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter patient ID:");
        return scanner.scanPositiveInt();
    }

    private void createBillFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        BillSAXHandler billSAXHandler = new BillSAXHandler();
        HospitalSAXParser saxParser = new HospitalSAXParser(billSAXHandler);

        try {
            saxParser.parse(xmlFilePath);
            List<Bill> bills = billSAXHandler.getBills();
            if (bills != null && !bills.isEmpty()) {
                for (Bill bill : bills) {
                    billService.create(
                        bill.getPatientId(),
                        bill.getAmount(),
                        bill.getBillingDate(),
                        bill.getPaymentStatus()
                    );
                }
                LOGGER.info("Bills created successfully from XML file.");
            } else {
                LOGGER.info("No bills found in the XML file.");
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing XML file: " + e.getMessage());
        }
    }

    private void updateBill() {
        LOGGER.info("Enter bill ID you want to update:");
        long id = scanner.scanPositiveInt();

        long patientId = getPatientId();

        double amount = getAmount();

        LocalDate billingDate = getLocalDate();

        PaymentStatus status = selectPaymentStatus();

        try {
            billService.update(id, patientId, amount, billingDate, status);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Update failed \n" + e);
        }
    }

    private void deleteBill() {
        LOGGER.info("Enter bill ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            billService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private PaymentStatus selectPaymentStatus() {
        PaymentStatus[] values = PaymentStatus.values();
        for (int i = 0; i < values.length; i++) {
            LOGGER.info(i + " - " + values[i].name());
        }

        int choice = scanner.scanInt(0, values.length);

        return values[choice];
    }

    private void printBills() {
        LOGGER.info(billService.findAll());
    }
}
