package com.solvd.hospital.sax.parser.handlers;

import com.solvd.hospital.entities.doctor.Doctor;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class DoctorSAXHandler extends DefaultHandler {
    @Getter
    private List<Doctor> doctors;
    private Doctor currentDoctor;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;

        if ("doctor".equals(currentElement)) {
            currentDoctor = new Doctor();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty() && currentDoctor != null) {
            if ("firstName".equals(currentElement)) {
                currentDoctor.setFirstName(value);
            } else if ("lastName".equals(currentElement)) {
                currentDoctor.setLastName(value);
            } else if ("specialization".equals(currentElement)) {
                currentDoctor.setSpecialization(value);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("doctor".equals(qName)) {
            if (doctors == null) {
                doctors = new ArrayList<>();
            }
            doctors.add(currentDoctor);
            currentDoctor = null;
        }
    }
}
