package com.solvd.hospital.menus;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.common.input.InputScanner;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.menus.handlers.PatientMenuHandler;
import com.solvd.hospital.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
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
        this.patientService = new PatientService();
        this.insuranceService = new InsuranceService();
        this.appointmentService = new AppointmentService();
        this.doctorService = new DoctorService();
        this.prescriptionService = new PrescriptionService();
        this.diagnosisService = new PatientDiagnosisService();
        this.hospitalizationService = new HospitalizationService();
        this.billService = new BillService();
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

            choice = scanner.scanInt(0, 8);

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
                    unpaidBills();
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
        } catch (EntityNotFoundException | RelatedEntityNotFound e) {
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

    private void unpaidBills() {
        displayUnpaidBills();

        int choice;

        LOGGER.info("1 - Pay the Bill");
        LOGGER.info("2 - Display unpaid Bills");
        LOGGER.info("0 - Back");

        choice = scanner.scanInt(0, 2);

        switch (choice) {
            case 1:
                payTheBill();
                break;
            case 2:
                displayUnpaidBills();
                break;
            case 0:
                LOGGER.info("Exiting...");
                break;
        }
    }

    private void displayUnpaidBills() {
        try {
            LOGGER.info(billService.findByPatientIdAndPaymentStatus(patient.getId(), PaymentStatus.UNPAID));
        } catch (EntityNotFoundException e) {
            LOGGER.info("You have no unpaid Bills");
        }
    }

    private void payTheBill() {
        LOGGER.info("Enter Bill ID:");
        int id = scanner.scanPositiveInt();

        Bill bill;
        try {
            bill = billService.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
            return;
        }

        Insurance insurance = null;
        try {
            insurance = insuranceService.findById(patient.getId());
        } catch (EntityNotFoundException ignored) {}

        double amount = bill.getAmount();

        if (insurance == null) {
            processBillWithoutInsurance(amount);
        } else if (insurance.getExpirationDate().isBefore(LocalDate.now())) {
            processExpiredInsurance(amount);
        } else if (insurance.getCoverageAmount() <= amount) {
            processCoveredBill(amount, insurance.getCoverageAmount());
        } else {
            processFullyCoveredBill(insurance, amount);
        }

        try {
            LOGGER.info(billService.update(id, patient.getId(), amount, LocalDate.now(), PaymentStatus.PAID));
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
        }
    }

    private void processBillWithoutInsurance(double amount) {
        LOGGER.info("You don't have insurance");
        LOGGER.info("Payment is " + amount + " BYN");
    }

    private void processExpiredInsurance(double amount) {
        LOGGER.info("Your insurance has expired");
        LOGGER.info("Payment is " + amount + " BYN");

        if (!deleteInsurance()) {
            LOGGER.error("Error deleting insurance.");
        }
    }

    private void processCoveredBill(double amount, double coverageAmount) {
        double remainingAmount = amount - coverageAmount;
        LOGGER.info("Payment is " + remainingAmount + " BYN");

        if (!deleteInsurance()) {
            LOGGER.error("Error deleting insurance.");
        }
    }

    private void processFullyCoveredBill(Insurance insurance, double amount) {
        insurance.setCoverageAmount(insurance.getCoverageAmount() - amount);
        try {
            insuranceService.update(
                    insurance.getPatientId(),
                    insurance.getPolicyNumber(),
                    insurance.getExpirationDate(),
                    insurance.getCoverageAmount(),
                    insurance.getType(),
                    insurance.getInsuranceProvider());
        } catch (EntityNotFoundException e) {
            LOGGER.error("Error updating insurance.");
        }

        LOGGER.info("Your insurance fully covered the bill");
    }

    private boolean deleteInsurance() {
        try {
            insuranceService.delete(patient.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
            return false;
        }
        return true;
    }
}
