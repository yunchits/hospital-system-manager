package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PatientDiagnosis {
    private long patientId;
    private Diagnosis diagnosis;

    @Override
    public String toString() {
        return String.format(
            "Patient ID: %d, Diagnosis: %s",
            patientId, diagnosis
        );
    }
}
