package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.sax.parser.HospitalSAXParser;
import com.solvd.hospital.sax.parser.handlers.DoctorSAXHandler;
import com.solvd.hospital.services.DoctorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DoctorMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(DoctorMenuHandler.class);

    private final InputScanner scanner;

    private final DoctorService doctorService;

    public DoctorMenuHandler() {
        this.scanner = new InputScanner();
        this.doctorService = new DoctorService();
    }

    @Override
    public void display() {
        printDoctors();

        int choice;
        do {
            LOGGER.info("Doctor Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    printDoctors();
                    break;
                case 3:
                    updateDoctor();
                    break;
                case 4:
                    deleteDoctors();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createDoctor() {
        LOGGER.info("Choose source for doctor creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (SAX)");
        int choice = scanner.scanInt(1, 2);

        if (choice == 1) {
            createDoctorFromConsole();
        } else if (choice == 2) {
            createDoctorFromXML();
        }
    }

    public Doctor createDoctorFromConsole() {
        LOGGER.info("Enter Doctor's First Name:");
        String firstName = scanner.scanName();

        LOGGER.info("Enter Doctor's Last Name:");
        String lastName = scanner.scanName();

        LOGGER.info("Enter Doctor's Specialization:");
        String specialization = scanner.scanString();

        while (true) {
            LOGGER.info("Enter Doctor's username:");
            String username = scanner.scanString();

            LOGGER.info("Enter Doctor's password:");
            String password = scanner.scanString();

            try {
                return doctorService.createWithUser(firstName, lastName, specialization, username, password);
            } catch (EntityAlreadyExistsException e) {
                LOGGER.error(e);
            }
        }
    }

    private void createDoctorFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        DoctorSAXHandler doctorSAXHandler = new DoctorSAXHandler();
        HospitalSAXParser saxParser = new HospitalSAXParser(doctorSAXHandler);

        try {
            saxParser.parse(xmlFilePath);
            List<Doctor> doctors = doctorSAXHandler.getDoctors();
            if (doctors != null && !doctors.isEmpty()) {
                for (Doctor doctor : doctors) {
                    doctorService.create(
                        doctor.getFirstName(),
                        doctor.getLastName(),
                        doctor.getSpecialization()
                    );
                }
                LOGGER.info("Doctors created successfully from XML file.");
            } else {
                LOGGER.info("No doctors found in the XML file.");
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing XML file: " + e.getMessage());
        }
    }

    private void updateDoctor() {
        LOGGER.info("Enter Doctor ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Doctor's First Name:");
        String firstName = scanner.scanName();

        LOGGER.info("Enter Doctor's Last Name:");
        String lastName = scanner.scanName();

        LOGGER.info("Enter Doctor's Specialization:");
        String specialization = scanner.scanString();

        try {
            doctorService.update(id, firstName, lastName, specialization);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteDoctors() {
        LOGGER.info("Enter doctor ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            doctorService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printDoctors() {
        LOGGER.info(doctorService.findAll());
    }
}
