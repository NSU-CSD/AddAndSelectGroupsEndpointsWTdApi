package com.krpo.krpo.exceptions;

import org.springframework.http.HttpStatus;

/*
 * an exception that holds a message and a http status code
 */
public class AppException extends RuntimeException {

    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
