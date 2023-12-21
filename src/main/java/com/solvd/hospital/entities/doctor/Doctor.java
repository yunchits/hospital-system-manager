package com.solvd.hospital.entities.doctor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Doctor {
    private long id;
    private String firstName;
    private String lastName;
    private String specialization;
}
