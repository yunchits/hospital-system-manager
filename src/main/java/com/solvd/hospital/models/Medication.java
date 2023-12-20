package com.solvd.hospital.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medication {
    private long id;
    private String medicationName;
    private String medicationDescription;
}
