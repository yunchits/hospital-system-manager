package com.solvd.hospital;

import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.repositories.PatientRepository;
import com.solvd.hospital.repositories.impl.PatientRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class PatientTests {

    public static void main(String[] args) {
        PatientRepository patientRepository = new PatientRepositoryImpl();

        Patient createdPatient = createPatient(patientRepository);
        displayPatientInfo("Created Patient", createdPatient);

        List<Patient> allPatients = getAllPatients(patientRepository);
        displayPatientInfo("All Patients", allPatients);

        Optional<Patient> foundPatient = findPatientById(patientRepository, createdPatient.getId());
        displayPatientInfo("Found Patient", foundPatient.get());

        boolean isDeleted = deletePatient(patientRepository, createdPatient.getId());
        System.out.println("Is Patient Deleted: " + isDeleted);
    }

    private static Patient createPatient(PatientRepository patientRepository) {
        Patient patient = new Patient()
            .setFirstName("John")
            .setLastName("Doe")
            .setBirthDate(java.time.LocalDate.of(1990, 5, 15))
            .setGender(Gender.MALE);

        return patientRepository.create(patient);
    }

    private static List<Patient> getAllPatients(PatientRepository patientRepository) {
        return patientRepository.findAll();
    }

    private static Optional<Patient> findPatientById(PatientRepository patientRepository, long id) {
        return patientRepository.findById(id);
    }

    private static boolean deletePatient(PatientRepository patientRepository, long id) {
        return patientRepository.delete(id);
    }

    private static void displayPatientInfo(String title, Object info) {
        System.out.println(title + ": " + info);
    }
}
