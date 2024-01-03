package com.solvd.hospital.common.exceptions;

public class AuthenticationException extends HospitalException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
