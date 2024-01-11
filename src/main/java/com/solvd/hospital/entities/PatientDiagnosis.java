package com.solvd.hospital.entities;

import com.solvd.hospital.xml.jaxb.adapters.DiagnosisAdapter;
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
public class PatientDiagnosis {

    private long patientId;

    @XmlJavaTypeAdapter(DiagnosisAdapter.class)
    @XmlElement(name = "diagnosisId")
    private Diagnosis diagnosis;

    @Override
    public String toString() {
        return String.format(
            "%nPatient ID: [%d], Diagnosis ID: [%d]: %s",
            patientId, diagnosis.getId(), diagnosis.getName()
        );
    }
}
