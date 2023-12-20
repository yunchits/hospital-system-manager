package com.solvd.hospital.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diagnose {
    private long id;
    private String diagnosisName;
    private String diagnosisDescription;
}
