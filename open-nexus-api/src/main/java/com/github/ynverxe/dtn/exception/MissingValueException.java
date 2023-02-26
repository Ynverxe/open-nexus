package com.github.ynverxe.dtn.exception;

public class MissingValueException extends RuntimeException {
    public MissingValueException() {
    }

    public MissingValueException(String message) {
        super(message);
    }
}