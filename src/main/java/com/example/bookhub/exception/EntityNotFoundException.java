package com.example.bookhub.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
