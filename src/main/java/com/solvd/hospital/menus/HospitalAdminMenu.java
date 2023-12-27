package com.solvd.hospital.menus;

import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.menus.handlers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HospitalAdminMenu implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(HospitalAdminMenu.class);

    private final InputScanner scanner;

    public HospitalAdminMenu() {
        this.scanner = new InputScanner();
    }

    @Override
    public void display() {
        int choice;
        do {
            LOGGER.info("Hospital Management System");
            LOGGER.info("1 - Appointments Management");
            LOGGER.info("2 - Bills Management");
            LOGGER.info("3 - Diagnosis Management");
            LOGGER.info("4 - Doctors Management");
            LOGGER.info("5 - Doctor's Salaries Management");
            LOGGER.info("6 - Hospitalizations Management");
            LOGGER.info("7 - Medications Management");
            LOGGER.info("8 - Patients Management");
            LOGGER.info("9 - Patient's Diagnosis Management");
            LOGGER.info("10 - Patient Insurances Management");
            LOGGER.info("11 - Prescriptions Management");
            LOGGER.info("0 - Exit");

            choice = scanner.scanInt(0, 11);

            switch (choice) {
                case 1:
                    new AppointmentMenuHandler().display();
                    break;
                case 2:
                    new BillMenuHandler().display();
                    break;
                case 3:
                    new DiagnosisMenuHandler().display();
                    break;
                case 4:
                    new DoctorMenuHandler().display();
                    break;
                case 5:
                    new DoctorSalaryMenuHandler().display();
                    break;
                case 6:
                    new HospitalizationMenuHandler().display();
                    break;
                case 7:
                    new MedicationMenuHandler().display();
                    break;
                case 8:
                    new PatientMenuHandler().display();
                    break;
                case 9:
                    new PatientDiagnosisMenuHandler().display();
                    break;
                case 10:
                    new InsuranceMenuHandler().display();
                    break;
                case 11:
                    new PrescriptionMenuHandler().display();
                    break;
                case 0:
                    LOGGER.info("Exiting...");
                    break;
            }
        } while (choice != 0);
    }
}
