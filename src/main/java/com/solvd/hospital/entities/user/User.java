package com.solvd.hospital.entities.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class User {
    private long id;

    @NotEmpty(message = "Username must not be empty")
    @Size(max = 45, message = "Username must not exceed 45 characters")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(max = 255, message = "Password must not exceed 255 characters")
    private String password;

    private Role role;
}
