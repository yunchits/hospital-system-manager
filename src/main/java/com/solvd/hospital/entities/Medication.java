package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Medication {
    private long id;
    private String medicationName;
    private String medicationDescription;
}
