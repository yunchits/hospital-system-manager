package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.MedicationService;
import com.solvd.hospital.services.PatientService;
import com.solvd.hospital.services.PrescriptionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrescriptionMenuHandler implements Menu {

    private static final Logger LOGGER = LogManager.getLogger(PrescriptionMenuHandler.class);

    private final InputScanner scanner;

    private final PrescriptionService prescriptionService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MedicationService medicationService;

    public PrescriptionMenuHandler() {
        this.scanner = new InputScanner();
        this.prescriptionService = new PrescriptionService();
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();
        this.medicationService = new MedicationService();
    }

    @Override
    public void display() {
        printPrescriptions();

        int choice;
        do {
            LOGGER.info("Prescription Management");
            MenuMessages.printCrudMenuOptions();

            choice = scanner.scanInt(0, 4);

            switch (choice) {
                case 1:
                    LOGGER.info(createPrescription());
                    break;
                case 2:
                    printPrescriptions();
                    break;
                case 3:
                    updatePrescription();
                    break;
                case 4:
                    deletePrescription();
                    break;
                case 0:
                    LOGGER.info("Returning to Main Menu...");
                    break;
            }
        } while (choice != 0);
    }

    private Prescription createPrescription() {
        LOGGER.info(doctorService.findAll());
        LOGGER.info("Enter Doctor ID from list:");
        long doctorId = scanner.scanPositiveInt();

        Doctor doctor = null;
        try {
            doctor = doctorService.findById(doctorId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Wrong Doctor ID");
        }

        LOGGER.info(patientService.findAll());
        LOGGER.info("Enter Patient ID from list:");
        long patientId = scanner.scanPositiveInt();

        Patient patient = null;
        try {
            patient = patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Patient Doctor ID");
        }

        LOGGER.info("Enter Medication ID from list:");
        LOGGER.info(medicationService.findAll());
        long medicationId = scanner.scanPositiveInt();

        Medication medication = null;
        try {
            medication = medicationService.findById(medicationId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Patient Medication ID");
        }

        return prescriptionService.create(doctor, patient, medication);
    }

    private void updatePrescription() {
        LOGGER.info("Enter Prescription ID to update:");
        long id = scanner.scanPositiveInt();

        LOGGER.info(doctorService.findAll());
        LOGGER.info("Enter Doctor ID from list:");
        long doctorId = scanner.scanPositiveInt();

        Doctor doctor = null;
        try {
            doctor = doctorService.findById(doctorId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Wrong Doctor ID");
        }

        LOGGER.info(patientService.findAll());
        LOGGER.info("Enter Patient ID from list:");
        long patientId = scanner.scanPositiveInt();

        Patient patient = null;
        try {
            patient = patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Patient Doctor ID");
        }

        LOGGER.info(medicationService.findAll());
        LOGGER.info("Enter Medication ID from list:");
        long medicationId = scanner.scanPositiveInt();

        Medication medication = null;
        try {
            medication = medicationService.findById(medicationId);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Patient Medication ID");
        }

        try {
            prescriptionService.update(id, doctor, patient, medication);
        } catch (EntityNotFoundException e) {
            LOGGER.info("Update failed\n" + e);
        }
    }

    private void deletePrescription() {
        LOGGER.info("Enter Prescription ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            prescriptionService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed \n" + e);
        }
    }

    private void printPrescriptions() {
        LOGGER.info(prescriptionService.findAll());
    }
}
