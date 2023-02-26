package com.github.ynverxe.dtn.exception;

public class SingletonClassException extends RuntimeException {
    public SingletonClassException(Class<?> clazz) {
        super("cannot re-instantiate " + clazz.toString());
    }
}
