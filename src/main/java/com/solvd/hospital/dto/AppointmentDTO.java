package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDTO {
    private long id;

    @JsonProperty("patientId")
    private long patientId;

    @JsonProperty("doctorId")
    private long doctorId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    @JsonProperty("appointmentDateTime")
    private LocalDateTime appointmentDateTime;
}
