package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.InsuranceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class InsuranceMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(InsuranceMenuHandler.class);

    private final InputScanner scanner;

    private final InsuranceService insuranceService;

    public InsuranceMenuHandler() {
        this.scanner = new InputScanner();
        this.insuranceService = new InsuranceService();
    }


    @Override
    public void display() {
        printInsurance();

        int choice;
        do {
            LOGGER.info("Insurance Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createHospitalization());
                    break;
                case 2:
                    printInsurance();
                    break;
                case 3:
                    updateInsurance();
                    break;
                case 4:
                    deleteInsurance();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private Insurance createHospitalization() {
        LOGGER.info("Enter Patient ID:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Policy Number:");
        String policyNumber = scanner.scanString();

        LOGGER.info("Enter Insurance Expiration Date:");
        LocalDate expirationDate = scanner.scanLocalDate();

        LOGGER.info("Enter Coverage Amount:");
        double coverageAmount = scanner.scanPositiveDouble();

        InsuranceType type = selectInsuranceType();

        LOGGER.info("Enter Insurance Provider:");
        String insuranceProvider = scanner.scanString();

        try {
            return insuranceService.create(id, policyNumber, expirationDate, coverageAmount, type, insuranceProvider);
        } catch (DuplicateKeyException | RelatedEntityNotFound e) {
            LOGGER.info("Creation failed\n" + e);
        }
        return null;
    }

    private void updateInsurance() {
        LOGGER.info("Enter Patient ID to update Insurance:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Policy Number:");
        String policyNumber = scanner.scanString();

        LOGGER.info("Enter Insurance Expiration Date:");
        LocalDate expirationDate = scanner.scanLocalDate();

        LOGGER.info("Enter Coverage Amount:");
        double coverageAmount = scanner.scanPositiveDouble();

        InsuranceType type = selectInsuranceType();

        LOGGER.info("Enter Insurance Provider:");
        String insuranceProvider = scanner.scanString();

        try {
            insuranceService.update(id, policyNumber, expirationDate, coverageAmount, type, insuranceProvider);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteInsurance() {
        LOGGER.info("Enter the patient ID whose insurance you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            insuranceService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printInsurance() {
        LOGGER.info(insuranceService.findAll());
    }

    private InsuranceType selectInsuranceType() {
        InsuranceType[] values = InsuranceType.values();

        for (int i = 0; i < values.length; i++) {
            LOGGER.info(i + " - " + values[i]);
        }

        int choice = scanner.scanInt(0, values.length);

        return values[choice];
    }
}
