package com.solvd.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solvd.hospital.entities.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {

    @JsonProperty("user")
    private User user;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("specialization")
    private String specialization;

}
