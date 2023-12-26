package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.PatientDiagnosisService;
import com.solvd.hospital.services.impl.PatientDiagnosisServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PatientDiagnosisMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(PatientDiagnosisMenuHandler.class);

    private final InputScanner scanner;

    private final PatientDiagnosisService patientDiagnosisService;

    public PatientDiagnosisMenuHandler() {
        this.scanner = new InputScanner();
        this.patientDiagnosisService = new PatientDiagnosisServiceImpl();
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
                    createAppointment();
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

    private void createAppointment() {
        LOGGER.info("Enter Patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Diagnosis ID:");
        long diagnosisId = scanner.scanPositiveInt();

        try {
            patientDiagnosisService.create(patientId, diagnosisId);
        } catch (RelatedEntityNotFound e) {
            LOGGER.error("Creation failed \n" + e);
        }
    }

    private void updatePatientDiagnosis() {
        LOGGER.info("Enter Patient ID you want to update:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Diagnosis ID you want to update:");
        long diagnosisId = scanner.scanPositiveInt();

        try {
            patientDiagnosisService.update(patientId, diagnosisId);
        } catch (RelatedEntityNotFound e) {
            LOGGER.error("Update failed \n" + e);
        }
    }

    private void deletePatientDiagnosis() {
        LOGGER.info("Enter Patient ID you want to delete:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter Diagnosis ID you want to delete:");
        long diagnosisId = scanner.scanPositiveInt();

        try {
            patientDiagnosisService.delete(patientId, diagnosisId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Update failed \n" + e);
        }
    }

    private void printPatientDiagnosis() {
        LOGGER.info(patientDiagnosisService.findAll());
    }
}