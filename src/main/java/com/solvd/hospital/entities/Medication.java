package com.solvd.hospital.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Medication {
    private long id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return String.format(
            "%n[%d] - Medication: %s, Description: %s",
            id, name, description
        );
    }
}
