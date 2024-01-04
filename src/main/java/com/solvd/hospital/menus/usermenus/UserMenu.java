package com.solvd.hospital.menus.usermenus;

import com.solvd.hospital.common.exceptions.AuthenticationException;
import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.entities.user.Role;
import com.solvd.hospital.entities.user.User;
import com.solvd.hospital.menus.Menu;
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

    public void registerPatient() {
        if (isDataInSystem()) {
            registerPatientWithExistingData();
        } else {
            registerNewPatient();
        }
    }

    private boolean isDataInSystem() {
        LOGGER.info("Are your data already in the system? (true/false)");
        return scanner.scanBoolean();
    }

    private void registerPatientWithExistingData() {
        LOGGER.info("Enter your personal ID:");
        int id = scanner.scanPositiveInt();

        LOGGER.info("Enter username:");
        String username = scanner.scanString();

        LOGGER.info("Enter password:");
        String password = scanner.scanString();

        try {
            Patient patient = patientService.findById(id);
            if (patient.getUserId() != 0) {
                LOGGER.info("This patient already have user ID");
                return;
            }

            User user = userService.register(username, password, Role.PATIENT);

            Patient updatedPatient = patientService.updateUserId(patient.getId(), user.getId());
            new PatientMenu(updatedPatient).display();
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            LOGGER.info("Registration failed\n" + e);
            display();
        }
    }

    private void registerNewPatient() {
        Patient patient = new PatientMenuHandler().createPatientFromConsole();
        new PatientMenu(patient).display();
    }

    private void registerDoctor() {
        if (isDataInSystem()) {
            registerDoctorWithExistingData();
        } else {
            registerNewDoctor();
        }
    }

    private void registerDoctorWithExistingData() {
        LOGGER.info("Enter your personal ID:");
        int id = scanner.scanPositiveInt();

        LOGGER.info("Enter username:");
        String username = scanner.scanString();

        LOGGER.info("Enter password:");
        String password = scanner.scanString();

        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor.getUserId() != 0) {
                LOGGER.info("This doctor already have user ID");
                return;
            }

            User user = userService.register(username, password, Role.DOCTOR);

            Doctor updatedDoctor = doctorService.updateUserId(doctor.getId(), user.getId());
            new DoctorMenu(updatedDoctor).display();
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            LOGGER.info("Registration failed\n" + e);
            display();
        }
    }

    private static void registerNewDoctor() {
        Doctor doctor = new DoctorMenuHandler().createDoctorFromConsole();
        new DoctorMenu(doctor).display();
    }
}
