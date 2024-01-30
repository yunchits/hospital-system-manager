package com.solvd.hospital.xml.jaxb.adapters;

import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.services.DoctorService;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class DoctorAdapter extends XmlAdapter<Long, Doctor> {
    private final DoctorService doctorService = new DoctorService();

    @Override
    public Doctor unmarshal(Long id) throws Exception {
        return doctorService.findById(id);
    }

    @Override
    public Long marshal(Doctor doctor) {
        return (doctor != null) ? doctor.getId() : null;
    }
}
