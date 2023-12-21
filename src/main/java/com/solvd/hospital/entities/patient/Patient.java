package com.solvd.hospital.entities.patient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Patient {
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
}
