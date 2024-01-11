package com.solvd.hospital.xml.jaxb.adapters;

import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.services.PatientService;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class PatientAdapter extends XmlAdapter<Long, Patient> {
    private final PatientService patientService = new PatientService();

    @Override
    public Patient unmarshal(Long id) throws Exception {
        return patientService.findById(id);
    }

    @Override
    public Long marshal(Patient patient) {
        return (patient != null) ? patient.getId() : null;
    }
}
