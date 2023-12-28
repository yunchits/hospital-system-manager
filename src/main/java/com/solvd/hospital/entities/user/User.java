package com.solvd.hospital.entities.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class User {
    private long id;
    private String username;
    private String password;
    private Role role;
}
