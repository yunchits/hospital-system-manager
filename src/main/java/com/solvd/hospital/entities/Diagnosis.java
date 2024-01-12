package com.solvd.hospital.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Diagnosis {
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @Override
    public String toString() {
        return String.format("%nDiagnosis [%d] - %s: %s", id, name, description);
    }
}
