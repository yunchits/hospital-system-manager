package com.solvd.hospital.entities;

import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Prescription {
    private long id;
    private Doctor doctor;
    private Patient patient;
    private Medication medication;

    @Override
    public String toString() {
        return String.format(
            "%nPrescription [%d] - Medication: %s (Doctor: %s, Patient: %s)",
                id, medication,
                doctor.getFirstName() + " " + doctor.getLastName(),
                patient.getFirstName() + " " + patient.getLastName()
        );
    }
}
