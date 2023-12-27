package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DoctorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

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
            LOGGER.info("Diagnosis Management");
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
        LOGGER.info("Enter Doctor's First Name:");
        String firstName = scanner.scanName();

        LOGGER.info("Enter Doctor's Last Name:");
        String lastName = scanner.scanName();

        LOGGER.info("Enter Doctor's Specialization:");
        String specialization = scanner.scanString();

        LOGGER.info("Enter Doctor's Salary:");
        double salary = scanner.scanPositiveDouble();

        LOGGER.info("Enter Salary Payment Date:");
        LocalDate date = scanner.scanLocalDate();

        doctorService.create(firstName, lastName, specialization, salary, date);
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

        LOGGER.info("Enter Doctor's Salary:");
        double salary = scanner.scanPositiveDouble();

        LOGGER.info("Enter Salary Payment Date:");
        LocalDate date = scanner.scanLocalDate();

        try {
            doctorService.update(id, firstName, lastName, specialization, salary, date);
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
