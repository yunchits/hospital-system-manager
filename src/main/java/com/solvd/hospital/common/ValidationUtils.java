package com.solvd.hospital.common;

import com.solvd.hospital.common.exceptions.InvalidArgumentException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateStringNotBlank(String value, String fieldName) throws InvalidArgumentException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidArgumentException(fieldName + " must not be blank");
        }
    }

    public static void validateStringLength(String value, String fieldName, int maxLength) throws InvalidArgumentException {
        if (value != null && value.length() > maxLength) {
            throw new InvalidArgumentException(fieldName + " must be less than or equal to " + maxLength + " characters");
        }
    }
}
