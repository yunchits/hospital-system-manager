package com.solvd.hospital.entities.patient;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Accessors(chain = true)
public class Patient {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedBirthDate = birthDate.format(formatter);

        return String.format("%nPatient [%d] - %s %s (Birth Date: %s, Gender: %s)",
            id, firstName, lastName, formattedBirthDate, gender);
    }
}
