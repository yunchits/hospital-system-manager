package com.solvd.hospital.xml.jaxb;

import com.solvd.hospital.entities.*;
import com.solvd.hospital.entities.patient.Insurance;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlJAXBFileHandler {

    private final JAXBContext jaxbContext;

    public XmlJAXBFileHandler() throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(Hospital.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> read(String path, Class<T> tClass) throws JAXBException, IOException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Hospital hospital = (Hospital) unmarshaller.unmarshal(new FileReader(path));
        List<T> result = new ArrayList<>();

        if (hospital != null) {
            if (tClass.equals(Medication.class)) {
                result.addAll((List<T>) hospital.getMedications());
            } else if (tClass.equals(PatientDiagnosis.class)) {
                result.addAll((List<T>) hospital.getPatientDiagnoses());
            } else if (tClass.equals(Insurance.class)) {
                result.addAll((List<T>) hospital.getInsurances());
            } else if (tClass.equals(Diagnosis.class)) {
                result.addAll((List<T>) hospital.getDiagnoses());
            } else if (tClass.equals(Prescription.class)) {
                result.addAll((List<T>) hospital.getPrescriptions());
            }
        }
        return result;
    }
}
