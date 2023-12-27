package com.solvd.hospital.entities.doctor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Accessors(chain = true)
public class DoctorSalary {
    private long id;
    private double salary;
    private LocalDate paymentDate;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedPaymentDate = paymentDate.format(formatter);

        return String.format("%nSalary [%d] - Salary: %.2f, Payment Date: %s",
            id, salary, formattedPaymentDate);
    }
}
