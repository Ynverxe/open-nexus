package com.github.ynverxe.dtn.exception;

public class UnknownImplicitlyModelReferenceException extends RuntimeException implements SuppresableException {

    public UnknownImplicitlyModelReferenceException() {
    }

    public UnknownImplicitlyModelReferenceException(String message) {
        super(message);
    }
}