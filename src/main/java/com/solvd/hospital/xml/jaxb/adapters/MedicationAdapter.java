package com.solvd.hospital.xml.jaxb.adapters;

import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.services.MedicationService;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class MedicationAdapter extends XmlAdapter<Long, Medication> {
    private final MedicationService medicationService = new MedicationService();

    @Override
    public Medication unmarshal(Long id) throws Exception {
        return medicationService.findById(id);
    }

    @Override
    public Long marshal(Medication medication) {
        return (medication != null) ? medication.getId() : null;
    }
}
