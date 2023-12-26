package com.solvd.hospital.menus;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.services.*;
import com.solvd.hospital.services.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class DoctorMenu implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(DoctorMenu.class);

    private final InputScanner scanner;

    private Doctor doctor;

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DiagnosisService diagnosisService;
    private final PatientDiagnosisService patientDiagnosisService;
    private final BillService billService;
    private final HospitalizationService hospitalizationService;
    private final PrescriptionService prescriptionService;
    private final MedicationService medicationService;

    private static final double DIAGNOSIS_COST = 50.0;
    private static final double PRESCRIPTION_COST = 25.0;
    private static final double HOSPITALIZATION_COST = 100.0;

    public DoctorMenu() {
        this.scanner = new InputScanner();
        this.doctorService = new DoctorServiceImpl();
        this.appointmentService = new AppointmentServiceImpl();
        this.diagnosisService = new DiagnosisServiceImpl();
        this.patientDiagnosisService = new PatientDiagnosisServiceImpl();
        this.billService = new BillServiceImpl();
        this.hospitalizationService = new HospitalizationServiceImpl();
        this.prescriptionService = new PrescriptionServiceImpl();
        this.medicationService = new MedicationServiceImpl();
    }

    @Override
    public void display() {
        login();

        int choice;
        do {
            LOGGER.info("1 - Display My Information");
            LOGGER.info("2 - Display Appointments");
            LOGGER.info("3 - Start Appointment");
            LOGGER.info("0 - Exit");

            choice = scanner.scanInt(0, 3);

            switch (choice) {
                case 1:
                    displayDoctorInfo();
                    break;
                case 2:
                    displayAppointments();
                    break;
                case 3:
                    startAppointment();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 0:
                    LOGGER.info("Exiting...");
                    break;
            }
        } while (choice != 0);
    }

    private void login() {
        LOGGER.info("Enter your personal ID:");
        int id = scanner.scanPositiveInt();

        try {
            this.doctor = doctorService.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Doctor with this ID doesn't exist, please enter existing ID: ");
            login();
        }
    }

    private void displayDoctorInfo() {
        LOGGER.info(doctor);
    }

    private void displayAppointments() {
        try {
            LOGGER.info(appointmentService.findByDoctorId(doctor.getId()));
        } catch (EntityNotFoundException e) {
            LOGGER.error("You don't have any appointments yet");
        }
    }

    private void startAppointment() {
        displayAppointments();

        LOGGER.info("Enter appointment ID");
        long appointmentId = scanner.scanPositiveInt();

        Appointment appointment = getAppointment(appointmentId);

        if (appointment == null) {
            return;
        }

        Patient patient = appointment.getPatient();

        appointmentMenu(patient);
    }

    private void appointmentMenu(Patient patient) {
        double billingAmount = 0;

        int choice;
        do {
            LOGGER.info("1 - Diagnose the Patient");
            LOGGER.info("2 - Write out a prescription for medications");
            LOGGER.info("3 - Hospitalize the patient");
            LOGGER.info("0 - Complete the appointment and set the Bill");

            choice = scanner.scanInt(0, 3);

            switch (choice) {
                case 1:
                    diagnosePatient(patient);
                    billingAmount += DIAGNOSIS_COST;
                    break;
                case 2:
                    createPrescription(patient);
                    billingAmount += PRESCRIPTION_COST;
                    break;
                case 3:
                    hospitalizePatient(patient);
                    billingAmount += HOSPITALIZATION_COST;
                    break;
                case 0:
                    LOGGER.info("You have completed your appointment with the patient:");
                    LOGGER.info(patient);
                    Bill bill = createBill(patient.getId(), billingAmount);
                    LOGGER.info("He will be billed for the amount: " + bill.getAmount());
                    break;
            }
        } while (choice != 0);
    }

    private void diagnosePatient(Patient patient) {
        LOGGER.info("Enter diagnosis number from list:");
        List<Diagnosis> diagnosisList = diagnosisService.findAll();
        LOGGER.info(diagnosisList);
        int diagnosisId = scanner.scanInt(0, diagnosisList.size());

        try {
            LOGGER.info(patientDiagnosisService.create(patient.getId(), diagnosisId));
        } catch (RelatedEntityNotFound e) {
            LOGGER.error("Diagnosis creation failed\n" + e);
        }
    }

    private void createPrescription(Patient patient) {
        List<Medication> medications = medicationService.findAll();
        LOGGER.info(medications);
        LOGGER.info("Enter medication ID for which you want to write a prescription");
        int medicationId = scanner.scanInt(0, medications.size());

        Medication medication = null;
        try {
             medication = medicationService.findById(medicationId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Wrong medication ID");
            createPrescription(patient);
        }

        prescriptionService.create(doctor, patient, medication);
    }

    private void hospitalizePatient(Patient patient) {
        LOGGER.info("Enter Admission Date:");
        LocalDate admissionDate = scanner.scanLocalDate();

        LOGGER.info("Enter Discharge Date:");
        LocalDate dischargeDate = scanner.scanLocalDate();

        try {
            hospitalizationService.create(patient.getId(), admissionDate, dischargeDate);
        } catch (EntityNotFoundException | RelatedEntityNotFound e) {
            LOGGER.error("Failed to create hospitalization record\n" + e);
        }
    }

    private Appointment getAppointment(long id) {
        try {
            Appointment appointment = appointmentService.findById(id);

            if (appointment.getDoctor().getId() != doctor.getId()) {
                throw new EntityNotFoundException();
            }

            return appointment;
        } catch (EntityNotFoundException e) {
            LOGGER.info("You don't have appointment with this ID");
        }
        return null;
    }

    private Bill createBill(long id, double billingAmount) {
        return billService.create(
            id,
            billingAmount,
            LocalDate.now(),
            PaymentStatus.UNPAID);
    }
}