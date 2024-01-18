package com.solvd.hospital.common.exceptions;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
