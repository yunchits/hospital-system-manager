package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Diagnosis {
    private long id;
    private String diagnosisName;
    private String diagnosisDescription;
}
