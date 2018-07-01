package com.domclick.exception;

public class RollbackException extends Exception {
    protected String message;

    public RollbackException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}