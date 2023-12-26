package com.solvd.hospital.entities.doctor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Doctor {
    private long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private DoctorSalary salary;

    @Override
    public String toString() {
        return String.format("%n[%d] - Dr. %s %s (Specialization: %s)",
            id, firstName, lastName, specialization);
    }
}
