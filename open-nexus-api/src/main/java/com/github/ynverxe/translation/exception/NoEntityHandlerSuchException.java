package com.github.ynverxe.translation.exception;

public class NoEntityHandlerSuchException extends RuntimeException {
    public NoEntityHandlerSuchException(Class<?> entityClass) {
        super("No entity handler such for: " + entityClass + " or parent");
    }
}
