package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.json.JsonFileHandler;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.MedicationService;
import com.solvd.hospital.xml.jaxb.XmlJAXBFileHandler;
import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class MedicationMenuHandler implements Menu {
    private static final Logger LOGGER = LogManager.getLogger(MedicationMenuHandler.class);

    private final InputScanner scanner;

    private final MedicationService medicationService;

    public MedicationMenuHandler() {
        this.scanner = new InputScanner();
        this.medicationService = new MedicationService();
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
                    createMedication();
                    break;
                case 2:
                    printMedications();
                    break;
                case 3:
                    updateMedication();
                    break;
                case 4:
                    deleteMedication();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createMedication() {
        LOGGER.info("Choose source for appointment creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (JAXB)");
        LOGGER.info("3 - Read from JSON file (jackson)");
        int choice = scanner.scanInt(1, 3);

        switch (choice) {
            case 1:
                createMedicationFromConsole();
                break;
            case 2:
                createMedicationFromXML();
                break;
            case 3:
                createMedicationFromJSON();
                break;
        }
    }

    private void createMedicationFromJSON() {
        LOGGER.info("Enter JSON file path:");
        String path = scanner.scanString();

        try {
            JsonFileHandler jfh = new JsonFileHandler();
            List<Medication> medications = jfh.readFromJson(path, Medication.class);

            for (Medication medication : medications) {
                medicationService.create(medication.getName(), medication.getDescription());
            }
        } catch (IOException | HospitalException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createMedicationFromConsole() {
        String name = getName();

        String description = getDescription();

        try {
            medicationService.create(name, description);
        } catch (HospitalException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createMedicationFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        try {
            XmlJAXBFileHandler jaxbFileHandler = new XmlJAXBFileHandler();
            List<Medication> medications = jaxbFileHandler.read(xmlFilePath, Medication.class);

            for (Medication medication : medications) {
                medicationService.create(medication.getDescription(), medication.getName());
            }
            LOGGER.info("Medications created successfully from XML file");
        } catch (JAXBException | HospitalException | IOException e) {
            LOGGER.info("Creation failed: " + e.getMessage());
        }
    }

    private void updateMedication() {
        LOGGER.info("Enter medication ID to update:");
        long id = scanner.scanPositiveInt();

        String name = getName();

        String description = getDescription();

        try {
            medicationService.update(id, name, description);
        } catch (HospitalException e) {
            LOGGER.info("Update failed: " + e.getMessage());
        }
    }

    private void deleteMedication() {
        LOGGER.info("Enter medication ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            medicationService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed: " + e.getMessage());
        }
    }

    private String getDescription() {
        LOGGER.info("Enter medication description:");
        return scanner.scanString();
    }

    private String getName() {
        LOGGER.info("Enter medication name:");
        return scanner.scanString();
    }

    private void printMedications() {
        LOGGER.info(medicationService.findAll());
    }
}
