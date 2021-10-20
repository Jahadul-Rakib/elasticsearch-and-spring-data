package com.database_example.exception;

public class APIException extends Exception {
    public APIException(String message, String exceptionReason) {
        super(message, new Throwable(exceptionReason));
    }
}
