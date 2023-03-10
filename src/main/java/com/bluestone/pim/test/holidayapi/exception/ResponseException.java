package com.bluestone.pim.test.holidayapi.exception;

/**
 * The class ResponseException
 */
public class ResponseException extends RuntimeException {

    /**
     * Constant statusCode
     */
    private int statusCode;

    /**
     * Default constructor of ResponseException
     */
    public ResponseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
