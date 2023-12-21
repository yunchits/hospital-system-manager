package com.solvd.hospital.entities.doctor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DoctorSalary {
    private long id;
    private long doctorId;
    private double salary;
    private LocalDate paymentDate;
}
