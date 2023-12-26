package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.BillService;
import com.solvd.hospital.services.impl.BillServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class BillMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(BillMenuHandler.class);

    private final InputScanner scanner;

    private final BillService billService;

    public BillMenuHandler() {
        this.scanner = new InputScanner();
        this.billService = new BillServiceImpl();
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
        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter billing amount:");
        double amount = scanner.scanPositiveDouble();

        LOGGER.info("Enter billing date:");
        LocalDate billingDate = scanner.scanLocalDate();

        PaymentStatus status = selectPaymentStatus();

        billService.create(patientId, amount, billingDate, status);
    }

    private void updateBill() {
        LOGGER.info("Enter bill ID you want to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter billing amount:");
        double amount = scanner.scanPositiveDouble();

        LOGGER.info("Enter billing date:");
        LocalDate billingDate = scanner.scanLocalDate();

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
