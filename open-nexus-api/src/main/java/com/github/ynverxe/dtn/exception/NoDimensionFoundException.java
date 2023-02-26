package com.github.ynverxe.dtn.exception;

public class NoDimensionFoundException extends Exception implements SuppresableException {
    public NoDimensionFoundException(String message) {
        super(message);
    }
}
