package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.MedicationService;
import com.solvd.hospital.services.impl.MedicationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicationMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(MedicationMenuHandler.class);

    private final InputScanner scanner;

    private final MedicationService medicationService;

    public MedicationMenuHandler() {
        this.scanner = new InputScanner();
        this.medicationService = new MedicationServiceImpl();
    }


    @Override
    public void display() {
        printMedications();

        int choice;
        do {
            LOGGER.info("Medication Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createHospitalization());
                    break;
                case 2:
                    printMedications();
                    break;
                case 3:
                    updateHospitalization();
                    break;
                case 4:
                    deleteHospitalization();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private Medication createHospitalization() {
        LOGGER.info("Enter Medication Name:");
        String name = scanner.scanString();

        LOGGER.info("Enter Medication Description:");
        String description = scanner.scanString();

        return medicationService.create(name, description);
    }

    private void updateHospitalization() {
        LOGGER.info("Enter Medication ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Medication Name:");
        String name = scanner.scanString();

        LOGGER.info("Enter Medication Description:");
        String description = scanner.scanString();

        try {
            medicationService.update(id, name, description);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteHospitalization() {
        LOGGER.info("Enter Medication ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            medicationService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printMedications() {
        LOGGER.info(medicationService.findAll());
    }
}
