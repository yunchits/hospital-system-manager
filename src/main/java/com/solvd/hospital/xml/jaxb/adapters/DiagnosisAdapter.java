package com.solvd.hospital.xml.jaxb.adapters;

import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.services.DiagnosisService;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class DiagnosisAdapter extends XmlAdapter<Long, Diagnosis> {

    private final DiagnosisService service = new DiagnosisService();

    @Override
    public Diagnosis unmarshal(Long id) throws Exception {
        return service.findById(id);
    }

    @Override
    public Long marshal(Diagnosis diagnosis) {
        return (diagnosis != null) ? diagnosis.getId() : null;
    }
}
