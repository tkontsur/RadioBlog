package com.radioblog.controller;

public class ResponseException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public ResponseException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
