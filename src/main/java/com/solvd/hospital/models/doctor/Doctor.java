package com.solvd.hospital.models.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor {
    private long id;
    private String firstName;
    private String lastName;
    private String specialization;
}
