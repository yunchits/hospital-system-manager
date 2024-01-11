package com.solvd.hospital.menus.handlers;

import com.solvd.hospital.common.exceptions.EntityAlreadyExistsException;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.Hospital;
import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.MenuMessages;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.MedicationService;
import com.solvd.hospital.services.PatientService;
import com.solvd.hospital.services.PrescriptionService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

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
                    createPrescription();
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

    private void createPrescription() {
        LOGGER.info("Choose source for appointment creation:");
        LOGGER.info("1 - Console input");
        LOGGER.info("2 - Read from XML file (JAXB)");
        int choice = scanner.scanInt(1, 2);

        if (choice == 1) {
            createPrescriptionFromConsole();
        } else if (choice == 2) {
            createPrescriptionFromXML();
        }
    }

    private void createPrescriptionFromConsole() {
        long doctorId = getDoctorId();
        Doctor doctor = getDoctorById(doctorId);

        long patientId = getPatientId();
        Patient patient = getPatientById(patientId);

        long medicationId = getMedicationId();
        Medication medication = getMedicationById(medicationId);
        
        try {
            prescriptionService.create(doctor, patient, medication);
        } catch (EntityAlreadyExistsException e) {
            LOGGER.error("Creation failed: " + e.getMessage());
        }
    }

    private void createPrescriptionFromXML() {
        LOGGER.info("Enter XML file path:");
        String xmlFilePath = scanner.scanString();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Hospital.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Hospital hospital = (Hospital) unmarshaller.unmarshal(new FileReader(xmlFilePath));

            List<Prescription> prescriptions = hospital.getPrescriptions();

            for (Prescription prescription : prescriptions) {
                prescriptionService.create(
                        prescription.getDoctor(),
                        prescription.getPatient(),
                        prescription.getMedication()
                );
            }
            LOGGER.info("Prescription created successfully from XML file");
        } catch (JAXBException | FileNotFoundException | EntityAlreadyExistsException e) {
            LOGGER.info("Creation failed: " + e.getMessage());
        }
    }

    private void updatePrescription() {
        LOGGER.info("Enter Prescription ID to update:");
        long id = scanner.scanPositiveInt();

        long doctorId = getDoctorId();
        Doctor doctor = getDoctorById(doctorId);

        long patientId = getPatientId();
        Patient patient = getPatientById(patientId);

        long medicationId = getMedicationId();
        Medication medication = getMedicationById(medicationId);

        try {
            prescriptionService.update(id, doctor, patient, medication);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            LOGGER.info("Update failed: " + e.getMessage());
        }
    }

    private void deletePrescription() {
        LOGGER.info("Enter Prescription ID you want to delete:");
        long id = scanner.scanPositiveInt();

        try {
            prescriptionService.delete(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Delete failed: " + e.getMessage());
        }
    }

    private void printPrescriptions() {
        LOGGER.info(prescriptionService.findAll());
    }

    private long getDoctorId() {
        LOGGER.info(doctorService.findAll());
        LOGGER.info("Enter Doctor ID from list:");
        return scanner.scanPositiveInt();
    }

    private long getPatientId() {
        LOGGER.info(patientService.findAll());
        LOGGER.info("Enter Patient ID from list:");
        return scanner.scanPositiveInt();
    }

    private long getMedicationId() {
        LOGGER.info(medicationService.findAll());
        LOGGER.info("Enter Medication ID from list:");
        return scanner.scanPositiveInt();
    }

    private Medication getMedicationById(long medicationId) {
        try {
            return medicationService.findById(medicationId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Medication not found with ID: " + medicationId);
            return null;
        }
    }

    private Doctor getDoctorById(long doctorId) {
        try {
            return doctorService.findById(doctorId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Doctor not found with ID: " + doctorId);
            return null;
        }
    }

    private Patient getPatientById(long patientId) {
        try {
            return patientService.findById(patientId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Patient not found with ID: " + patientId);
            return null;
        }
    }
}
