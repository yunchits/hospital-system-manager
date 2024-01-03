package com.solvd.hospital.entities;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Hospital {

    @XmlElementWrapper(name = "medications")
    @XmlElement(name = "medication")
    private List<Medication> medications;

    @XmlElementWrapper(name = "patientDiagnoses")
    @XmlElement(name = "patientDiagnosis")
    private List<PatientDiagnosis> patientDiagnoses;
}
