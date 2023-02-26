package com.github.ynverxe.dtn.exception;

public class MapCreationException extends RuntimeException {

    public MapCreationException(String message) {
        super(message);
    }

    public MapCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapCreationException(Throwable cause) {
        super(cause);
    }
}