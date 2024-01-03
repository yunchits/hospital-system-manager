package com.solvd.hospital.entities;

import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Accessors(chain = true)
public class Hospitalization {
    private long id;
    private Patient patient;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String formattedAdmissionDate = admissionDate.format(formatter);
        String formattedDischargeDate = dischargeDate.format(formatter);

        return String.format("%nHospitalization [%d] - Patient: %s (Admission Date: %s, Discharge Date: %s)",
                id,
                patient.getFirstName() + " " + patient.getLastName(),
                formattedAdmissionDate,
                formattedDischargeDate
        );
    }
}
