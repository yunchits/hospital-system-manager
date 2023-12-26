package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.AppointmentService;
import com.solvd.hospital.services.impl.AppointmentServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class AppointmentMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentMenuHandler.class);

    private final InputScanner scanner;

    private final AppointmentService appointmentService;

    public AppointmentMenuHandler() {
        this.scanner = new InputScanner();
        this.appointmentService = new AppointmentServiceImpl();
    }

    @Override
    public void display() {
        printAppointments();

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
                    printAppointments();
                    break;
                case 3:
                    updateAppointment();
                    break;
                case 4:
                    deleteAppointment();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createAppointment() {
        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter doctor ID:");
        long doctorId = scanner.scanPositiveInt();

        LOGGER.info("Enter appointment date and time (dd.MM.yyyy HH:mm):");
        LocalDateTime appointmentDateTime = scanner.scanLocalDateTime();

        try {
            appointmentService.create(patientId, doctorId, appointmentDateTime);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Creation failed \n" + e);
        }
    }

    private void updateAppointment() {
        LOGGER.info("Enter appointment ID you want to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info("Enter doctor ID:");
        long doctorId = scanner.scanPositiveInt();

        LOGGER.info("Enter appointment date and time:");
        LocalDateTime appointmentDateTime = scanner.scanLocalDateTime();

        try {
            appointmentService.update(id, patientId, doctorId, appointmentDateTime);
        } catch (RelatedEntityNotFound | EntityNotFoundException e) {
            LOGGER.error("Update failed \n" + e);
        }
    }

    private void deleteAppointment() {
        LOGGER.info("Enter appointment ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            appointmentService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Update failed \n" + e);
        }
    }

    private void printAppointments() {
        LOGGER.info(appointmentService.findAll());
    }
}
