package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.parser.HospitalSAXParser;
import com.solvd.hospital.parser.handlers.AppointmentSAXHandler;
import com.solvd.hospital.services.AppointmentService;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentMenuHandler.class);

    private final InputScanner scanner;

    private final AppointmentService appointmentService;

    public AppointmentMenuHandler() {
        this.scanner = new InputScanner();
        this.appointmentService = new AppointmentService();
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
        LOGGER.info("Choose source for appointment creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file");
        int choice = scanner.scanInt(1, 2);

        if (choice == 1) {
            createAppointmentFromConsole();
        } else if (choice == 2) {
            createAppointmentFromXML();
        }
    }

    private void createAppointmentFromConsole() {
        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info(new DoctorService().findAll());
        LOGGER.info("Enter doctor ID:");
        long doctorId = scanner.scanPositiveInt();

        LOGGER.info("Enter appointment date and time (dd.MM.yyyy HH:mm):");
        LocalDateTime appointmentDateTime = scanner.scanLocalDateTime();

        try {
            appointmentService.create(patientId, doctorId, appointmentDateTime);
        } catch (EntityNotFoundException | InvalidArgumentException e) {
            LOGGER.error("Creation failed \n" + e);
        }
    }

    private void createAppointmentFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        AppointmentSAXHandler appointmentSAXHandler = new AppointmentSAXHandler();
        HospitalSAXParser saxParser = new HospitalSAXParser(appointmentSAXHandler);

        try {
            saxParser.parse(xmlFilePath);
            List<Appointment> appointments = appointmentSAXHandler.getAppointments();
            LOGGER.debug(appointments);
            if (appointments != null && !appointments.isEmpty()) {
                for (Appointment appointment : appointments) {
                    appointmentService.create(
                        appointment.getPatient().getId(),
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDateTime()
                    );
                }
                LOGGER.info("Appointments created successfully from XML file.");
            } else {
                LOGGER.info("No appointments found in the XML file.");
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing XML file: " + e.getMessage());
        }
    }

    private void updateAppointment() {
        LOGGER.info("Enter appointment ID you want to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter patient ID:");
        long patientId = scanner.scanPositiveInt();

        LOGGER.info(new DoctorService().findAll());
        LOGGER.info("Enter doctor ID:");
        long doctorId = scanner.scanPositiveInt();

        LOGGER.info("Enter appointment date and time:");
        LocalDateTime appointmentDateTime = scanner.scanLocalDateTime();

        try {
            appointmentService.update(id, patientId, doctorId, appointmentDateTime);
        } catch (RelatedEntityNotFound | EntityNotFoundException | InvalidArgumentException e) {
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
