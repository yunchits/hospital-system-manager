package com.solvd.hospital.entities.patient;

import com.solvd.hospital.xml.jaxb.adapters.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Insurance {
    private long patientId;
    private String policyNumber;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate expirationDate;
    private double coverageAmount;
    private InsuranceType type;
    private String insuranceProvider;

    @Override
    public String toString() {
        return String.format(
            "%n Patient Insurance [%d] - Policy Number: %s, Expiration Date: %s, Coverage Amount: %.2f, Type: %s, Provider: %s",
            patientId, policyNumber, expirationDate, coverageAmount, type, insuranceProvider
        );
    }
}
