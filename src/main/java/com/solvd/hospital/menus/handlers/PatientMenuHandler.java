package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.HospitalException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.PatientService;
import com.solvd.hospital.xml.sax.parser.HospitalSAXParser;
import com.solvd.hospital.xml.sax.parser.handlers.PatientSAXHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

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
                    createPatient();
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

    private void createPatient() {
        LOGGER.info("Choose source for patient creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (SAX)");
        int choice = scanner.scanInt(1, 2);

        switch (choice) {
            case 1:
                createPatientFromConsole();
                break;
            case 2:
                createPatientFromXML();
                break;
        }
    }

    public Patient createPatientFromConsole() {
        String firstName = getFirstName();

        String lastName = getLastName();

        LocalDate date = getLocalDate();

        Gender gender = selectGender();

        LOGGER.info("Enter patient's username:");
        String username = scanner.scanString();

        LOGGER.info("Enter patient's password:");
        String password = scanner.scanString();

        try {
            return patientService.createWithUser(firstName, lastName, date, gender, username, password);
        } catch (HospitalException e) {
            LOGGER.info("Creation failed: " + e);
        }
        return null;
    }

    private void createPatientFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        PatientSAXHandler patientSAXHandler = new PatientSAXHandler();
        HospitalSAXParser saxParser = new HospitalSAXParser(patientSAXHandler);

        try {
            saxParser.parse(xmlFilePath);
            List<Patient> patients = patientSAXHandler.getPatients();
            if (patients != null && !patients.isEmpty()) {
                for (Patient patient : patients) {
                    patientService.create(
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getBirthDate(),
                            patient.getGender()
                    );
                }
                LOGGER.info("Patients created successfully from XML file.");
            } else {
                LOGGER.info("No patients found in the XML file.");
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing XML file: " + e.getMessage());
        }
    }

    private void updatePatient() {
        LOGGER.info("Enter patient ID to update:");
        long id = scanner.scanPositiveInt();

        String firstName = getFirstName();

        String lastName = getLastName();

        LocalDate date = getLocalDate();

        Gender gender = selectGender();

        try {
            patientService.update(id, firstName, lastName, date, gender);
        } catch (HospitalException e) {
            LOGGER.info("Update failed: " + e.getMessage());
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
            LOGGER.error("Delete failed: " + e.getMessage());
        }
    }

    private void printPatients() {
        LOGGER.info(patientService.findAll());
    }

    private LocalDate getLocalDate() {
        LOGGER.info("Enter Patient's birth date (dd.MM.yyyy):");
        return scanner.scanLocalDate();
    }

    private String getLastName() {
        LOGGER.info("Enter patient's last name:");
        return scanner.scanName();
    }

    private String getFirstName() {
        LOGGER.info("Enter patient's first name:");
        return scanner.scanName();
    }
}
