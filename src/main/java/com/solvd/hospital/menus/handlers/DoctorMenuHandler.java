package com.solvd.hospital.menus.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.dto.DoctorDTO;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.user.User;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.UserService;
import com.solvd.hospital.xml.sax.parser.HospitalSAXParser;
import com.solvd.hospital.xml.sax.parser.handlers.DoctorSAXHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
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
        LOGGER.info("3 - Read from JSON file (jackson)");
        int choice = scanner.scanInt(1, 3);

        switch (choice) {
            case 1:
                createDoctorFromConsole();
                break;
            case 2:
                createDoctorFromXML();
                break;
            case 3:
                createDoctorFromJSON();
                break;
        }
    }

    private void createDoctorFromJSON() {
        LOGGER.info("Enter JSON file path:");
        String path = scanner.scanString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {

            File file = new File(path);

            JsonNode jsonNode = objectMapper.readTree(file);

            if (jsonNode.isArray()) {
                List<DoctorDTO> doctorDTOs = objectMapper.readValue(file, new TypeReference<>() {
                });

                for (DoctorDTO doctorDTO : doctorDTOs) {
                    createDoctor(doctorDTO);
                }
            } else {
                DoctorDTO doctorDTO = objectMapper.readValue(file, DoctorDTO.class);

                createDoctor(doctorDTO);
            }
        } catch (IOException | EntityNotFoundException | EntityAlreadyExistsException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createDoctor(DoctorDTO doctorDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        User user = doctorDTO.getUser();

        User registered = new UserService().register(
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );

        doctorService.createWithUser(
                doctorDTO.getFirstName(),
                doctorDTO.getLastName(),
                doctorDTO.getSpecialization(),
                registered.getId()
        );
    }

    public Doctor createDoctorFromConsole() {
        String firstName = getFirstName();

        String lastName = getLastName();

        String specialization = getSpecialization();

        while (true) {
            LOGGER.info("Enter doctor's username:");
            String username = scanner.scanString();

            LOGGER.info("Enter doctor's password:");
            String password = scanner.scanString();

            try {
                return doctorService.createWithUser(firstName, lastName, specialization, username, password);
            } catch (EntityAlreadyExistsException e) {
                LOGGER.error("Creation failed: " + e.getMessage());
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
        LOGGER.info("Enter doctor ID to update:");
        long id = scanner.scanPositiveInt();

        String firstName = getFirstName();

        String lastName = getLastName();

        String specialization = getSpecialization();

        try {
            doctorService.update(id, firstName, lastName, specialization);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed: " + e.getMessage());
        }
    }

    private String getSpecialization() {
        LOGGER.info("Enter doctor's specialization:");
        return scanner.scanString();
    }

    private String getLastName() {
        LOGGER.info("Enter doctor's last name:");
        return scanner.scanName();
    }

    private String getFirstName() {
        LOGGER.info("Enter doctor's first name:");
        return scanner.scanName();
    }

    private void deleteDoctors() {
        LOGGER.info("Enter doctor ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            doctorService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed: " + e.getMessage());
        }
    }

    private void printDoctors() {
        LOGGER.info(doctorService.findAll());
    }
}
