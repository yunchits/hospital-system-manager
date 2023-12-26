package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DoctorSalaryService;
import com.solvd.hospital.services.impl.DoctorSalaryServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class DoctorSalaryMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(DoctorSalaryMenuHandler.class);

    private final InputScanner scanner;

    private final DoctorSalaryService doctorSalaryService;

    public DoctorSalaryMenuHandler() {
        this.scanner = new InputScanner();
        this.doctorSalaryService = new DoctorSalaryServiceImpl();
    }


    @Override
    public void display() {
        printSalaries();

        int choice;
        do {
            LOGGER.info("Doctor Salary Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createHospitalization());
                    break;
                case 2:
                    printSalaries();
                    break;
                case 3:
                    updateSalary();
                    break;
                case 4:
                    deleteSalary();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private DoctorSalary createHospitalization() {
        LOGGER.info("Enter Salary:");
        double salary = scanner.scanPositiveDouble();

        LOGGER.info("Enter Payment Date:");
        LocalDate paymentDate = scanner.scanLocalDate();

        return doctorSalaryService.create(salary, paymentDate);
    }

    private void updateSalary() {
        LOGGER.info("Enter Salary ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info("Enter Salary:");
        double salary = scanner.scanPositiveDouble();

        LOGGER.info("Enter Payment Date:");
        LocalDate paymentDate = scanner.scanLocalDate();

        try {
            doctorSalaryService.update(id, salary, paymentDate);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deleteSalary() {
        LOGGER.info("Enter Doctor Salary ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            doctorSalaryService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printSalaries() {
        LOGGER.info(doctorSalaryService.findAll());
    }
}
