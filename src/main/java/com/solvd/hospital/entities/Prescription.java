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
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%nPrescription [%d] - [%d] Medication: %s (", id, medication.getId(), medication.getName()));

        if (doctor != null) {
            builder.append(String.format("[%d] Doctor: %s", doctor.getId(), doctor.getFirstName() + " " + doctor.getLastName()));
        } else {
            builder.append("[Doctor: null]");
        }

        builder.append(")");
        return builder.toString();
    }
}
