package com.solvd.hospital.parser.handlers;

import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientSAXHandler extends DefaultHandler {

    @Getter
    private List<Patient> patients;
    private Patient currentPatient;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;

        if ("patient".equals(currentElement)) {
            currentPatient = new Patient();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty() && currentPatient != null) {
            if ("firstName".equals(currentElement)) {
                currentPatient.setFirstName(value);
            } else if ("lastName".equals(currentElement)) {
                currentPatient.setLastName(value);
            } else if ("birthDate".equals(currentElement)) {
                currentPatient.setBirthDate(LocalDate.parse(value));
            } else if ("gender".equals(currentElement)) {
                currentPatient.setGender(Gender.valueOf(value));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("patient".equals(qName)) {
            if (patients == null) {
                patients = new ArrayList<>();
            }
            patients.add(currentPatient);
            currentPatient = null;
        }
    }
}
