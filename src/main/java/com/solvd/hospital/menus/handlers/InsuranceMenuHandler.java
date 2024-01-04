package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.DuplicateKeyException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Hospital;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.InsuranceService;
import com.solvd.hospital.services.PatientService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

public class InsuranceMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(InsuranceMenuHandler.class);

    private final InputScanner scanner;

    private final InsuranceService insuranceService;

    public InsuranceMenuHandler() {
        this.scanner = new InputScanner();
        this.insuranceService = new InsuranceService();
    }


    @Override
    public void display() {
        printInsurance();

        int choice;
        do {
            LOGGER.info("Insurance Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    createInsurance();
                    break;
                case 2:
                    printInsurance();
                    break;
                case 3:
                    updateInsurance();
                    break;
                case 4:
                    deleteInsurance();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private void createInsurance() {
        LOGGER.info("Choose source for patient creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (JAXB)");
        int choice = scanner.scanInt(1, 2);

        if (choice == 1) {
            createInsuranceFromConsole();
        } else if (choice == 2) {
            createInsuranceFromXML();
        }
    }

    private void createInsuranceFromConsole() {
        LOGGER.info(new PatientService().findAll());
        LOGGER.info("Enter patient ID:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter policy number:");
        String policyNumber = scanner.scanString();

        LOGGER.info("Enter insurance expiration date (dd.MM.yyyy):");
        LocalDate expirationDate = scanner.scanLocalDate();

        LOGGER.info("Enter coverage amount:");
        double coverageAmount = scanner.scanPositiveDouble();

        InsuranceType type = selectInsuranceType();

        LOGGER.info("Enter insurance provider:");
        String insuranceProvider = scanner.scanString();

        try {
            insuranceService.create(id, policyNumber, expirationDate, coverageAmount, type, insuranceProvider);
        } catch (DuplicateKeyException | RelatedEntityNotFound e) {
            LOGGER.info("Creation failed\n" + e);
        }
    }

    private void createInsuranceFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Hospital.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Hospital hospital = (Hospital) unmarshaller.unmarshal(new FileReader(xmlFilePath));

            List<Insurance> insurances = hospital.getInsurances();
            LOGGER.info(insurances);

            for (Insurance insurance : insurances) {
                insuranceService.create(
                        insurance.getPatientId(),
                        insurance.getPolicyNumber(),
                        insurance.getExpirationDate(),
                        insurance.getCoverageAmount(),
                        insurance.getType(),
                        insurance.getInsuranceProvider()
                );
            }
            LOGGER.info("Insurances created successfully from XML file.");
        } catch (JAXBException | RelatedEntityNotFound | FileNotFoundException | DuplicateKeyException e) {
            e.printStackTrace();
            LOGGER.info("Creation failed\n" + e);
        }
    }

    private void updateInsurance() {
        LOGGER.info("Enter patient ID to update insurance:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter policy number:");
        String policyNumber = scanner.scanString();

        LOGGER.info("Enter insurance expiration date (dd.MM.yyyy):");
        LocalDate expirationDate = scanner.scanLocalDate();

        LOGGER.info("Enter coverage amount:");
        double coverageAmount = scanner.scanPositiveDouble();

        InsuranceType type = selectInsuranceType();

        LOGGER.info("Enter insurance provider:");
        String insuranceProvider = scanner.scanString();

        try {
            insuranceService.update(id, policyNumber, expirationDate, coverageAmount, type, insuranceProvider);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteInsurance() {
        LOGGER.info("Enter the patient ID whose insurance you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            insuranceService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printInsurance() {
        LOGGER.info(insuranceService.findAll());
    }

    private InsuranceType selectInsuranceType() {
        InsuranceType[] values = InsuranceType.values();

        for (int i = 0; i < values.length; i++) {
            LOGGER.info(i + " - " + values[i]);
        }

        int choice = scanner.scanInt(0, values.length);

        return values[choice];
    }
}
