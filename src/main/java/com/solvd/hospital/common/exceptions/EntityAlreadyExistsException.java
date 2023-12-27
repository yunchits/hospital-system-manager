package com.solvd.hospital.common.exceptions;

public class EntityAlreadyExistsException extends HospitalException {
    public EntityAlreadyExistsException() {
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
