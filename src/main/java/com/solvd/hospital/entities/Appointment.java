package com.solvd.hospital.entities;

import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Accessors(chain = true)
public class Appointment {
    private long id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = appointmentDateTime.format(formatter);

        return String.format("%nAppointment [%d] - Date and Time: %s (Patient: %s, Doctor: %s)",
                id,
                formattedDateTime,
                patient.getFirstName() + " " + patient.getLastName(),
                doctor.getFirstName() + " " + doctor.getLastName()
        );
    }
}

