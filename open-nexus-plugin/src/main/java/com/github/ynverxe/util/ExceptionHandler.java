package com.github.ynverxe.util;

import java.util.function.Function;

public class ExceptionHandler<T> implements Function<Throwable, T> {
    @Override
    public T apply(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }
}
