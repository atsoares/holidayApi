package com.bluestone.pim.test.holidayapi.exception;

public class ResponseException extends RuntimeException {
    private int statusCode;

    public ResponseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
