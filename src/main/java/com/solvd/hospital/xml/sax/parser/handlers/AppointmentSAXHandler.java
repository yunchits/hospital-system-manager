package com.solvd.hospital.xml.sax.parser.handlers;

import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSAXHandler extends DefaultHandler {

    @Getter
    private List<Appointment> appointments;
    private Appointment currentAppointment;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;
        if ("appointment".equals(currentElement)) {
            currentAppointment = new Appointment();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty() && currentAppointment != null) {
            if ("patientId".equals(currentElement)) {
                currentAppointment.setPatient(new Patient().setId(Long.parseLong(value)));
            } else if ("doctorId".equals(currentElement)) {
                currentAppointment.setDoctor(new Doctor().setId(Long.parseLong(value)));
            } else if ("appointmentDateTime".equals(currentElement)) {
                currentAppointment.setAppointmentDateTime(LocalDateTime.parse(value));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("appointment".equals(qName)) {
            if (appointments == null) {
                appointments = new ArrayList<>();
            }
            appointments.add(currentAppointment);
            currentAppointment = null;
        }
    }
}
