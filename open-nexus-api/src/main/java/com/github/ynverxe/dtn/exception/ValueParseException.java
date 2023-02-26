package com.github.ynverxe.dtn.exception;

import com.github.ynverxe.structured.exception.PathHolderException;
import org.jetbrains.annotations.Nullable;

public class ValueParseException extends PathHolderException {

    public ValueParseException() {
    }

    public ValueParseException(String message) {
        super(message);
    }

    public ValueParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueParseException(Throwable cause) {
        super(cause);
    }

    public ValueParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValueParseException setPath(@Nullable String path) {
        return (ValueParseException)super.setPath(path);
    }

    public ValueParseException insertMissingPath(String path) {
        return (ValueParseException)super.insertMissingPath(path);
    }
}