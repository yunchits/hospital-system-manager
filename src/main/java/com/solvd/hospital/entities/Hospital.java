package com.solvd.hospital.entities;

import com.solvd.hospital.entities.patient.Insurance;
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

    @XmlElementWrapper(name = "insurances")
    @XmlElement(name = "insurance")
    private List<Insurance> insurances;

    @XmlElementWrapper(name = "diagnoses")
    @XmlElement(name = "diagnosis")
    private List<Diagnosis> diagnoses;

    @XmlElementWrapper(name = "prescriptions")
    @XmlElement(name = "prescription")
    private List<Prescription> prescriptions;
}
