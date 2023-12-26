package com.solvd.hospital.menus;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.handlers.PatientMenuHandler;
import com.solvd.hospital.services.*;
import com.solvd.hospital.services.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class PatientMenu implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(PatientMenu.class);

    private final InputScanner scanner;

    private Patient patient;

    private final PatientService patientService;
    private final InsuranceService insuranceService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final PatientDiagnosisService diagnosisService;
    private final HospitalizationService hospitalizationService;
    private final BillService billService;

    public PatientMenu() {
        this.scanner = new InputScanner();
        this.patientService = new PatientServiceImpl();
        this.insuranceService = new InsuranceServiceImpl();
        this.appointmentService = new AppointmentServiceImpl();
        this.doctorService = new DoctorServiceImpl();
        this.prescriptionService = new PrescriptionServiceImpl();
        this.diagnosisService = new PatientDiagnosisServiceImpl();
        this.hospitalizationService = new HospitalizationServiceImpl();
        this.billService = new BillServiceImpl();
    }

    @Override
    public void display() {
        loginOrRegister();

        int choice;
        do {
            LOGGER.info("1 - Display My Information");
            LOGGER.info("2 - Make an Appointment");
            LOGGER.info("3 - Display Appointments");
            LOGGER.info("4 - Display Insurance Information");
            LOGGER.info("5 - Display Prescriptions");
            LOGGER.info("6 - Display Diagnoses");
            LOGGER.info("7 - Display Hospitalization history");
            LOGGER.info("8 - Display unpaid Bills");
            LOGGER.info("0 - Exit");

            choice = scanner.scanInt(0, 7);

            switch (choice) {
                case 1:
                    displayPatientInfo();
                    break;
                case 2:
                    LOGGER.info(makeAppointment());
                    break;
                case 3:
                    displayAppointments();
                    break;
                case 4:
                    displayInsuranceInfo();
                    break;
                case 5:
                    displayPrescriptions();
                    break;
                case 6:
                    displayDiagnoses();
                    break;
                case 7:
                    displayHospitalizations();
                    break;
                case 8:
//                    displayUnpaidBills();
                    break;
                case 0:
                    LOGGER.info("Exiting...");
                    break;
            }
        } while (choice != 0);
    }

    private void loginOrRegister() {
        int choice;

        LOGGER.info("Patient Menu");
        LOGGER.info("1 - Login");
        LOGGER.info("2 - Register");
        LOGGER.info("0 - Exit");

        choice = scanner.scanInt(0, 2);

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
        LOGGER.info("Enter your personal ID:");
        int id = scanner.scanPositiveInt();

        try {
            this.patient = patientService.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Patient with this ID doesn't exist, please register first: ");
            register();
        }
    }

    private void register() {
        this.patient = new PatientMenuHandler().createPatient();
    }

    private Appointment makeAppointment() {
        LOGGER.info(doctorService.findAll());

        LOGGER.info("Select the doctor you want to make an appointment with and enter his ID: ");
        long doctorId = scanner.scanPositiveInt();

        LOGGER.info("Enter desired appointment date and time (dd.MM.yyyy HH:mm): ");
        LocalDateTime appointmentDateTime = scanner.scanLocalDateTime();

        try {
            return appointmentService.create(patient.getId(), doctorId, appointmentDateTime);
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
        }
        return null;
    }

    private void displayInsuranceInfo() {
        Insurance insurance;

        try {
            insurance = insuranceService.findById(patient.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have insurance");
            return;
        }

        LOGGER.info(insurance);
    }

    private void displayAppointments() {
        try {
            LOGGER.info(appointmentService.findByPatientId(patient.getId()));
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have any appointments yet");
        }
    }

    private void displayPatientInfo() {
        LOGGER.info(patient);
    }

    private void displayHospitalizations() {
        try {
            LOGGER.info(hospitalizationService.findByPatientId(patient.getId()));
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have any hospitalizations");
        }
    }

    private void displayDiagnoses() {
        try {
            LOGGER.info(diagnosisService.findByPatientId(patient.getId()));
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have any diagnoses");
        }
    }

    private void displayPrescriptions() {
        try {
            LOGGER.info(prescriptionService.findByPatientId(patient.getId()));
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have any prescriptions");
        }
    }
}
