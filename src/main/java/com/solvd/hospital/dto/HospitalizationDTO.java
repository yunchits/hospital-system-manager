package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HospitalizationDTO {

    private long id;

    @JsonProperty("patientId")
    private long patientId;

    @JsonProperty("admissionDate")
    private LocalDate admissionDate;

    @JsonProperty("dischargeDate")
    private LocalDate dischargeDate;
}
