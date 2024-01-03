package com.solvd.hospital.menus;

import com.solvd.hospital.common.exceptions.AuthenticationException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;
import com.solvd.hospital.menus.handlers.DoctorMenuHandler;
import com.solvd.hospital.menus.handlers.PatientMenuHandler;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.PatientService;
import com.solvd.hospital.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserMenu implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(UserMenu.class);

    private final InputScanner scanner;
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public UserMenu() {
        this.scanner = new InputScanner();
        this.userService = new UserService();
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();
    }

    @Override
    public void display() {
        LOGGER.info("Login Menu");
        LOGGER.info("1 - Login");
        LOGGER.info("2 - Register");
        LOGGER.info("0 - Exit");

        int choice = scanner.scanInt(0, 2);

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                LOGGER.info("Exiting...");
                break;
        }
    }

    private void login() {
        LOGGER.info("Enter your login:");
        String username = scanner.scanString();

        LOGGER.info("Enter your password:");
        String password = scanner.scanString();

        User user;
        try {
            user = userService.login(username, password);
        } catch (AuthenticationException e) {
            LOGGER.error(e);
            display();
            return;
        }

        Role role = user.getRole();

        switch (role) {
            case DOCTOR:
                handleDoctorLogin(user.getId());
                break;
            case PATIENT:
                handlePatientLogin(user.getId());
                break;
        }
    }

    private void handleDoctorLogin(long userId) {
        try {
            Doctor doctor = doctorService.findByUserId(userId);
            new DoctorMenu(doctor).display();
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
            display();
        }
    }

    private void handlePatientLogin(long userId) {
        try {
            Patient patient = patientService.findByUserId(userId);
            new PatientMenu(patient).display();
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
            display();
        }
    }

    private void register() {
        LOGGER.info("Please specify if you are registering as a patient or a doctor");
        LOGGER.info("1 - Patient");
        LOGGER.info("2 - Doctor");

        int choice = scanner.scanInt(1, 2);

        switch (choice) {
            case 1:
                registerPatient();
                break;
            case 2:
                registerDoctor();
                break;
        }
    }

    private void registerPatient() {
        Patient patient = new PatientMenuHandler().createPatient();
        new PatientMenu(patient).display();
    }

    private void registerDoctor() {
        Doctor doctor = new DoctorMenuHandler().createDoctor();
        new DoctorMenu(doctor).display();
    }
}
