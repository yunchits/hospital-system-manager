package com.solvd.hospital.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
public class Medication {
    private long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 225, message = "Medication name must not exceed 225 characters")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 225, message = "Medication description must not exceed 225 characters")
    @JsonProperty("description")
    private String description;

    @Override
    public String toString() {
        return String.format("%nMedication [%d] - %s: %s", id, name, description);
    }
}
