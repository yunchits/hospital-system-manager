package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DiagnosisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiagnosisMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(DiagnosisMenuHandler.class);

    private final InputScanner scanner;

    private final DiagnosisService diagnosisService;

    public DiagnosisMenuHandler() {
        this.scanner = new InputScanner();
        this.diagnosisService = new DiagnosisService();
    }

    @Override
    public void display() {
        printDiagnosis();

        int choice;
        do {
            LOGGER.info("Diagnosis Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createDiagnosis();
                    break;
                case 2:
                    printDiagnosis();
                    break;
                case 3:
                    updateDiagnosis();
                    break;
                case 4:
                    deleteDiagnosis();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createDiagnosis() {
        LOGGER.info("Enter diagnosis name: ");
        String name = scanner.scanString();

        LOGGER.info("Enter diagnosis description:");
        String description = scanner.scanString();

        diagnosisService.create(name, description);
    }

    private void updateDiagnosis() {
        LOGGER.info("Enter diagnosis ID you want to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter diagnosis name: ");
        String name = scanner.scanString();

        LOGGER.info("Enter diagnosis description:");
        String description = scanner.scanString();

        try {
            diagnosisService.update(id, name, description);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteDiagnosis() {
        LOGGER.info("Enter diagnosis ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            diagnosisService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printDiagnosis() {
        LOGGER.info(diagnosisService.findAll());
    }
}
