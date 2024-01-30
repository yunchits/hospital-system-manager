package com.solvd.hospital.xml.sax.parser.handlers;

import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HospitalizationSAXHandler extends DefaultHandler {

    @Getter
    private List<Hospitalization> hospitalizations;
    private Hospitalization currentHospitalization;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;

        if ("hospitalization".equals(currentElement)) {
            currentHospitalization = new Hospitalization();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty() && currentHospitalization != null) {
            if ("patientId".equals(currentElement)) {
                currentHospitalization.setPatient(new Patient().setId(Long.parseLong(value)));
            } else if ("admissionDate".equals(currentElement)) {
                currentHospitalization.setAdmissionDate(LocalDate.parse(value));
            } else if ("dischargeDate".equals(currentElement)) {
                currentHospitalization.setDischargeDate(LocalDate.parse(value));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("hospitalization".equals(qName)) {
            if (hospitalizations == null) {
                hospitalizations = new ArrayList<>();
            }
            hospitalizations.add(currentHospitalization);
            currentHospitalization = null;
        }
    }
}
