package com.solvd.hospital.entities;

import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.jaxb.adapters.DoctorAdapter;
import com.solvd.hospital.jaxb.adapters.MedicationAdapter;
import com.solvd.hospital.jaxb.adapters.PatientAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Prescription {
    private long id;

    @XmlElement(name = "doctorId")
    @XmlJavaTypeAdapter(DoctorAdapter.class)
    private Doctor doctor;

    @XmlElement(name = "patientId")
    @XmlJavaTypeAdapter(PatientAdapter.class)
    private Patient patient;

    @XmlElement(name = "medicationId")
    @XmlJavaTypeAdapter(MedicationAdapter.class)
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
