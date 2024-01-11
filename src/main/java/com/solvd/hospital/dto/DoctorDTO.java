package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solvd.hospital.entities.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DoctorDTO {
    @Valid
    @NotNull(message = "User must not be null")
    @JsonProperty("user")
    private User user;

    @NotEmpty(message = "First name must not be empty")
    @Size(max = 45, message = "Doctor first name must not exceed 45 characters")
    @JsonProperty("firstName")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @Size(max = 45, message = "Doctor last name must not exceed 45 characters")
    @JsonProperty("lastName")
    private String lastName;

    @NotEmpty(message = "Specialization must not be empty")
    @Size(max = 45, message = "Doctor last name must not exceed 45 characters")
    @JsonProperty("specialization")
    private String specialization;
}
