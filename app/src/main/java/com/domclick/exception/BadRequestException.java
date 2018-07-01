package com.domclick.exception;

public class BadRequestException extends RollbackException {
    public BadRequestException(String message) {
        super(message);
    }
}
