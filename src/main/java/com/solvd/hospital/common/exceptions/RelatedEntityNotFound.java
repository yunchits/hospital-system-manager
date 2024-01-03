package com.solvd.hospital.common.exceptions;

public class RelatedEntityNotFound extends HospitalException {
    public RelatedEntityNotFound() {
    }

    public RelatedEntityNotFound(String message) {
        super(message);
    }
}
