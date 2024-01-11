package com.solvd.hospital.menus.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.dto.HospitalizationDTO;
import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.xml.sax.parser.HospitalSAXParser;
import com.solvd.hospital.xml.sax.parser.handlers.HospitalizationSAXHandler;
import com.solvd.hospital.services.HospitalizationService;
import com.solvd.hospital.services.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HospitalizationMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(HospitalizationMenuHandler.class);

    private final InputScanner scanner;

    private final HospitalizationService hospitalizationService;

    public HospitalizationMenuHandler() {
        this.scanner = new InputScanner();
        this.hospitalizationService = new HospitalizationService();
    }


    @Override
    public void display() {
        printHospitalization();

        int choice;
        do {
            LOGGER.info("Hospitalization Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createHospitalization();
                    break;
                case 2:
                    printHospitalization();
                    break;
                case 3:
                    updateHospitalization();
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

    private void createHospitalization() {
        LOGGER.info("Choose source for hospitalization creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (SAX)");
        LOGGER.info("3 - Read from JSON file (jackson)");
        int choice = scanner.scanInt(1, 3);

        switch (choice) {
            case 1:
                createHospitalizationFromConsole();
                break;
            case 2:
                createHospitalizationFromXML();
                break;
            case 3:
                createHospitalizationFromJSON();
                break;
        }
    }

    private void createHospitalizationFromJSON() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File("src/main/resources/json/hospitalization.json");

            HospitalizationDTO hospitalizationDTO = objectMapper.readValue(file, HospitalizationDTO.class);

            hospitalizationService.create(hospitalizationDTO);

        } catch (IOException | InvalidArgumentException | RelatedEntityNotFound e) {
            LOGGER.error("Creation failed\n" + e);
        }
    }

    private void createHospitalizationFromConsole() {
        long patientId = getPatientId();

        LocalDate admissionDate = getAdmissionDate();

        LocalDate dischargeDate = getDischargeDate();

        try {
            hospitalizationService.create(patientId, admissionDate, dischargeDate);
        } catch (RelatedEntityNotFound | InvalidArgumentException e) {
            LOGGER.error("Creation failed \n" + e);
        }
    }

    private void createHospitalizationFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        HospitalizationSAXHandler hospitalizationSAXHandler = new HospitalizationSAXHandler();
        HospitalSAXParser saxParser = new HospitalSAXParser(hospitalizationSAXHandler);

        try {
            saxParser.parse(xmlFilePath);
            List<Hospitalization> hospitalizations = hospitalizationSAXHandler.getHospitalizations();
            if (hospitalizations != null && !hospitalizations.isEmpty()) {
                for (Hospitalization hospitalization : hospitalizations) {
                    hospitalizationService.create(
                            hospitalization.getPatient().getId(),
                            hospitalization.getAdmissionDate(),
                            hospitalization.getDischargeDate()
                    );
                }
                LOGGER.info("Hospitalizations created successfully from XML file.");
            } else {
                LOGGER.info("No hospitalizations found in the XML file.");
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing XML file: " + e.getMessage());
        }
    }

    private void updateHospitalization() {
        LOGGER.info("Enter hospitalization ID to update:");
        long id = scanner.scanPositiveInt();

        long patientId = getPatientId();

        LocalDate admissionDate = getAdmissionDate();

        LocalDate dischargeDate = getDischargeDate();

        try {
            hospitalizationService.update(id, patientId, admissionDate, dischargeDate);
        } catch (EntityNotFoundException | RelatedEntityNotFound | InvalidArgumentException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteHospitalization() {
        LOGGER.info("Enter hospitalization ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            hospitalizationService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private LocalDate getDischargeDate() {
        LOGGER.info("Enter discharge date (dd.MM.yyyy):");
        return scanner.scanLocalDate();
    }

    private LocalDate getAdmissionDate() {
        LOGGER.info("Enter admission date (dd.MM.yyyy):");
        return scanner.scanLocalDate();
    }

    private long getPatientId() {
        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter patient ID:");
        return scanner.scanPositiveInt();
    }

    private void printHospitalization() {
        LOGGER.info(hospitalizationService.findAll());
    }
}
