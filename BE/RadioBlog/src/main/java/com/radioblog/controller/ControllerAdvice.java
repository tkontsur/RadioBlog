package com.radioblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    public ControllerAdvice() {
        super();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleResponseException(ResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
