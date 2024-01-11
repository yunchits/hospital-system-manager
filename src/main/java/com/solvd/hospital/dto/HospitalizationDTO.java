package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class HospitalizationDTO {
    private long id;

    @NotNull(message = "Patient ID must not be null")
    @JsonProperty("patientId")
    private long patientId;

    @NotNull(message = "Admission date must not be null")
    @Past(message = "Admission date must be in the past")
    @JsonProperty("admissionDate")
    private LocalDate admissionDate;

    @Future(message = "Discharge date must be in the future")
    @JsonProperty("dischargeDate")
    private LocalDate dischargeDate;
}
