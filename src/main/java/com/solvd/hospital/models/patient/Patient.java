package com.solvd.hospital.models.patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Patient {
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
}
