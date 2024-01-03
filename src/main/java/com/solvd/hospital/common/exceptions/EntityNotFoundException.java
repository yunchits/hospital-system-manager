package com.solvd.hospital.common.exceptions;

public class EntityNotFoundException extends HospitalException{
    public EntityNotFoundException() {
    }
    public EntityNotFoundException(String message) {
        super(message);
    }
}
