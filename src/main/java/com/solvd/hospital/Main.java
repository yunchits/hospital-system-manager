package com.solvd.hospital;

import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.repositories.AppointmentRepository;
import com.solvd.hospital.repositories.impl.AppointmentRepositoryImpl;
import com.solvd.hospital.repositories.impl.PatientRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl();

//        Appointment newAppointment = new Appointment();
//        newAppointment.setPatientId(1);
//        newAppointment.setDoctorId(1);
//        newAppointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
//
//        Appointment createdAppointment = appointmentRepository.create(newAppointment);
//
//        System.out.println( createdAppointment.toString());

//        long patientIdToRetrieve = createdAppointment.getPatientId();
        List<Appointment> retrievedAppointment = appointmentRepository.getByPatientId(1);

        System.out.println("Retrieved Appointment: " + retrievedAppointment);

        PatientRepositoryImpl patientRepository = new PatientRepositoryImpl();

        Patient patient = new Patient();

        patient.setFirstName("name");
        patient.setLastName("surname");
        patient.setGender(Gender.FEMALE);
        patient.setBirthDate(LocalDate.now().minusYears(10));

        Patient patient1 = patientRepository.create(patient);

        System.out.println(patient1);
    }
}
