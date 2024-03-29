package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.json.JsonFileHandler;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DiagnosisService;
import com.solvd.hospital.xml.jaxb.XmlJAXBFileHandler;
import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

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
        LOGGER.info("Choose source for patient diagnosis creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (JAXB)");
        LOGGER.info("3 - Read from JSON file (jackson)");
        int choice = scanner.scanInt(1, 3);

        switch (choice) {
            case 1:
                createDiagnosisFromConsole();
                break;
            case 2:
                createDiagnosisFromXML();
                break;
            case 3:
                createDiagnosisFromJSON();
                break;
        }
    }

    private void createDiagnosisFromJSON() {
        LOGGER.info("Enter JSON file path:");
        String path = scanner.scanString();

        try {
            JsonFileHandler jfh = new JsonFileHandler();
            List<Diagnosis> diagnoses = jfh.readFromJson(path, Diagnosis.class);

            for (Diagnosis diagnosis : diagnoses) {
                diagnosisService.create(diagnosis.getName(), diagnosis.getDescription());
            }

        } catch (IOException | HospitalException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createDiagnosisFromConsole() {
        LOGGER.info("Enter diagnosis name:");
        String name = scanner.scanString();

        LOGGER.info("Enter diagnosis description:");
        String description = scanner.scanString();

        try {
            diagnosisService.create(name, description);
        } catch (HospitalException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createDiagnosisFromXML() {
        LOGGER.info("Enter XML file path for diagnoses:");
        String xmlFilePath = scanner.scanString();

        try {
            XmlJAXBFileHandler jaxbFileHandler = new XmlJAXBFileHandler();
            List<Diagnosis> diagnoses = jaxbFileHandler.read(xmlFilePath, Diagnosis.class);

            for (Diagnosis diagnosis : diagnoses) {
                diagnosisService.create(
                        diagnosis.getName(),
                        diagnosis.getDescription()
                );
            }
            LOGGER.info("Diagnoses created successfully from XML file.");
        } catch (JAXBException | HospitalException | IOException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
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
        } catch (HospitalException e) {
            LOGGER.info("Update failed: " + e.getMessage());
        }
    }

    private void deleteDiagnosis() {
        LOGGER.info("Enter diagnosis ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            diagnosisService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed: " + e.getMessage());
        }
    }

    private void printDiagnosis() {
        LOGGER.info(diagnosisService.findAll());
    }
}
