package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.PatientDiagnosisService;
import com.solvd.hospital.services.PatientService;
import com.solvd.hospital.xml.jaxb.XmlJAXBFileHandler;
import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class PatientDiagnosisMenuHandler implements Menu {
    private static final Logger LOGGER = LogManager.getLogger(PatientDiagnosisMenuHandler.class);

    private final InputScanner scanner;

    private final PatientDiagnosisService patientDiagnosisService;

    public PatientDiagnosisMenuHandler() {
        this.scanner = new InputScanner();
        this.patientDiagnosisService = new PatientDiagnosisService();
    }

    @Override
    public void display() {
        printPatientDiagnosis();

        int choice;
        do {
            LOGGER.info("Appointment Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createPatientDiagnosis();
                    break;
                case 2:
                    printPatientDiagnosis();
                    break;
                case 3:
                    updatePatientDiagnosis();
                    break;
                case 4:
                    deletePatientDiagnosis();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createPatientDiagnosis() {
        LOGGER.info("Choose source for patient diagnosis creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (JAXB)");
        int choice = scanner.scanInt(1, 2);

        switch (choice) {
            case 1:
                createPatientDiagnosisFromConsole();
                break;
            case 2:
                createMedicationFromXML();
                break;
        }
    }

    private void createPatientDiagnosisFromConsole() {
        long patientId = getNewDiagnosisId();

        long diagnosisId = getDiagnosisId();

        try {
            patientDiagnosisService.create(patientId, diagnosisId);
        } catch (RelatedEntityNotFound | EntityAlreadyExistsException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createMedicationFromXML() {
        LOGGER.info("Enter XML file path for patient diagnoses:");
        String xmlFilePath = scanner.scanString();

        try {
            XmlJAXBFileHandler jaxbFileHandler = new XmlJAXBFileHandler();
            List<PatientDiagnosis> patientDiagnoses = jaxbFileHandler.read(xmlFilePath, PatientDiagnosis.class);

            for (PatientDiagnosis patientDiagnosis : patientDiagnoses) {
                patientDiagnosisService.create(patientDiagnosis.getPatientId(), patientDiagnosis.getDiagnosis().getId());
            }
            LOGGER.info("Patient diagnoses created successfully from XML file.");
        } catch (JAXBException | RelatedEntityNotFound | EntityAlreadyExistsException | IOException e) {
            LOGGER.info("Creation failed: " + e.getMessage());
        }
    }

    private void updatePatientDiagnosis() {
        long patientId = getPatientId();

        long diagnosisId = getDiagnosisId();

        long newDiagnosisId = getNewDiagnosisId();

        try {
            patientDiagnosisService.update(patientId, diagnosisId, newDiagnosisId);
        } catch (RelatedEntityNotFound | EntityAlreadyExistsException e) {
            LOGGER.error("Update failed: " + e.getMessage());
        }
    }

    private long getNewDiagnosisId() {
        LOGGER.info("Enter new Diagnosis ID:");
        return scanner.scanPositiveInt();
    }

    private long getDiagnosisId() {
        LOGGER.info("Enter Diagnosis ID you want to update:");
        return scanner.scanPositiveInt();
    }

    private long getPatientId() {
        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter Patient ID you want to update:");
        return scanner.scanPositiveInt();
    }

    private void deletePatientDiagnosis() {
        LOGGER.info("Enter Patient ID you want to delete:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Diagnosis ID you want to delete:");
        long diagnosisId = scanner.scanPositiveInt();

        try {
            patientDiagnosisService.delete(patientId, diagnosisId);
            LOGGER.info("The delete operation completed successfully");
        } catch (EntityNotFoundException | RelatedEntityNotFound e) {
            LOGGER.error("Update failed: " + e.getMessage());
        }
    }

    private void printPatientDiagnosis() {
        LOGGER.info(patientDiagnosisService.findAll());
    }
}
