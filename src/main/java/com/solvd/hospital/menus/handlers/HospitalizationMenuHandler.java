package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.HospitalizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class HospitalizationMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(HospitalizationMenuHandler.class);

    private final InputScanner scanner;

    private final HospitalizationService hospitalizationService;

    public HospitalizationMenuHandler() {
        this.scanner = new InputScanner();
        this.hospitalizationService = new HospitalizationService();
    }


    @Override
    public void display() {
        printHospitalization();

        int choice;
        do {
            LOGGER.info("Hospitalization Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createHospitalization());
                    break;
                case 2:
                    printHospitalization();
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

    private Hospitalization createHospitalization() {
        LOGGER.info("Enter Patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Admission Date:");
        LocalDate admissionDate = scanner.scanLocalDate();

        LOGGER.info("Enter Discharge Date:");
        LocalDate dischargeDate = scanner.scanLocalDate();

        try {
            return hospitalizationService.create(patientId, admissionDate, dischargeDate);
        } catch (RelatedEntityNotFound | InvalidArgumentException e) {
            LOGGER.error("Creation failed\n" + e);
        }
        return null;
    }

    private void updateHospitalization() {
        LOGGER.info("Enter Hospitalization ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Admission Date:");
        LocalDate admissionDate = scanner.scanLocalDate();

        LOGGER.info("Enter Discharge Date:");
        LocalDate dischargeDate = scanner.scanLocalDate();

        try {
            hospitalizationService.update(id, patientId, admissionDate, dischargeDate);
        } catch (EntityNotFoundException | RelatedEntityNotFound | InvalidArgumentException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteHospitalization() {
        LOGGER.info("Enter Hospitalization ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            hospitalizationService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printHospitalization() {
        LOGGER.info(hospitalizationService.findAll());
    }
}
