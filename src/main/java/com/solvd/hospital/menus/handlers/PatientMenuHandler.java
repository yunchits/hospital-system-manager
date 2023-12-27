package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class PatientMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(PatientMenuHandler.class);

    private final InputScanner scanner;

    private final PatientService patientService;

    public PatientMenuHandler() {
        this.scanner = new InputScanner();
        this.patientService = new PatientService();
    }


    @Override
    public void display() {
        printPatients();

        int choice;
        do {
            LOGGER.info("Patient Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createPatient());
                    break;
                case 2:
                    printPatients();
                    break;
                case 3:
                    updatePatient();
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

    public Patient createPatient() {
        LOGGER.info("Enter Patient's First Name:");
        String firstName = scanner.scanName();

        LOGGER.info("Enter Patient's Last Name:");
        String lastName = scanner.scanName();

        LOGGER.info("Enter Patient's birth date: ");
        LocalDate date = scanner.scanLocalDate();

        Gender gender = selectGender();

        return patientService.create(firstName, lastName, date, gender);
    }

    private void updatePatient() {
        LOGGER.info("Enter Patient ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Patient's First Name:");
        String firstName = scanner.scanName();

        LOGGER.info("Enter Patient's Last Name:");
        String lastName = scanner.scanName();

        LOGGER.info("Enter Patient's birth date: ");
        LocalDate date = scanner.scanLocalDate();

        Gender gender = selectGender();

        try {
            patientService.update(id, firstName, lastName, date, gender);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private Gender selectGender() {
        Gender[] values = Gender.values();

        for (int i = 0; i < values.length; i++) {
            LOGGER.info(i + " - " + values[i]);
        }

        int choice = scanner.scanInt(0, values.length);

        return values[choice];
    }

    private void deleteHospitalization() {
        LOGGER.info("Enter patient ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            patientService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printPatients() {
        LOGGER.info(patientService.findAll());
    }
}
