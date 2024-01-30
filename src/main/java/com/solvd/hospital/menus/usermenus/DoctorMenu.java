package com.solvd.hospital.menus.usermenus;

import com.solvd.hospital.common.exceptions.*;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.*;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class DoctorMenu implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(DoctorMenu.class);

    private final InputScanner scanner;

    private final Doctor doctor;

    private final AppointmentService appointmentService;
    private final BillingService billingService;

    public DoctorMenu(Doctor doctor) {
        this.doctor = doctor;
        this.scanner = new InputScanner();
        this.appointmentService = new AppointmentService();
        this.billingService = new BillingService();
    }

    @Override
    public void display() {
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
                case 0:
                    LOGGER.info("Exiting...");
                    break;
            }
        } while (choice != 0);
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
            LOGGER.info("Appointment not found");
            return;
        }

        appointmentMenu(appointment);
    }

    private void appointmentMenu(Appointment appointment) {
        Patient patient = appointment.getPatient();

        int choice;
        do {
            LOGGER.info("1 - Diagnose the patient");
            LOGGER.info("2 - Write out a prescription for medications");
            LOGGER.info("3 - Hospitalize the patient");
            LOGGER.info("0 - Complete the appointment and set the bill");

            choice = scanner.scanInt(0, 3);

            switch (choice) {
                case 1:
                    diagnosePatient(patient);
                    billingService.performOperation(BillingOperation.DIAGNOSIS);
                    break;
                case 2:
                    createPrescription(patient);
                    billingService.performOperation(BillingOperation.PRESCRIPTION);
                    break;
                case 3:
                    hospitalizePatient(patient);
                    billingService.performOperation(BillingOperation.HOSPITALIZATION);
                    break;
                case 0:
                    endAppointment(appointment, patient);
                    break;
            }
        } while (choice != 0);
    }

    private void endAppointment(Appointment appointment, Patient patient) {
        LOGGER.info("You have completed your appointment with the patient:");
        LOGGER.info(patient);

        Bill bill = billingService.createBill(patient.getId());
        LOGGER.info("He will be billed for the amount: " + bill.getAmount());
        try {
            appointmentService.delete(appointment.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void diagnosePatient(Patient patient) {
        int diagnosisId = getDiagnosisId();

        try {
            LOGGER.info(new PatientDiagnosisService().create(patient.getId(), diagnosisId));
        } catch (EntityAlreadyExistsException e) {
            LOGGER.info("Your patient already has this diagnosis");
        } catch (RelatedEntityNotFound e) {
            LOGGER.info(e.getMessage());
        }
    }

    private int getDiagnosisId() {
        LOGGER.info("Enter diagnosis number from list:");
        List<Diagnosis> diagnoses = new DiagnosisService().findAll();
        LOGGER.info(diagnoses);
        return scanner.scanInt(0, diagnoses.size());
    }

    private void createPrescription(Patient patient) {
        Medication medication = getMedication(patient);

        try {
            new PrescriptionService().create(doctor, patient, medication);
        } catch (EntityAlreadyExistsException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Medication getMedication(Patient patient) {
        MedicationService medicationService = new MedicationService();

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
        return medication;
    }

    private void hospitalizePatient(Patient patient) {
        LOGGER.info("Enter admission date:");
        LocalDate admissionDate = scanner.scanLocalDate();

        LOGGER.info("Enter discharge date:");
        LocalDate dischargeDate = scanner.scanLocalDate();

        try {
            new HospitalizationService().create(patient.getId(), admissionDate, dischargeDate);
        } catch (HospitalException e) {
            LOGGER.error("Failed to create hospitalization record: " + e.getMessage());
        }
    }

    private Appointment getAppointment(long id) {
        try {
            Appointment appointment = appointmentService.findById(id);

            if (appointment.getDoctor().getId() != doctor.getId()) {
                throw new RelatedEntityNotFound("Error");
            }

            return appointment;
        } catch (HospitalException e) {
            LOGGER.info("You don't have appointment with this ID");
        }
        return null;
    }
}
