package com.solvd.hospital.models.doctor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DoctorSalary {
    private long id;
    private long doctorsId;
    private double salary;
    private LocalDate paymentDate;
}
