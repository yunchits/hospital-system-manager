package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDTO {
    private long id;

    @JsonProperty("patientId")
    @Min(value = 1, message = "Patient ID must be greater than or equal to 1")
    private long patientId;

    @JsonProperty("doctorId")
    @Min(value = 1, message = "Doctor ID must be greater than or equal to 1")
    private long doctorId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    @JsonProperty("appointmentDateTime")
    @NotNull(message = "AppointmentDateTime must not be null")
    private LocalDateTime appointmentDateTime;
}
