package com.github.ynverxe.dtn.exception;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

public class ExceptionCatcherImpl implements ExceptionCatcher {

    private final List<Predicate<Exception>> exceptionHandlers = new ArrayList<>();

    @Override
    public <T> T catchException(Callable<T> supplier) {
        try {
            return supplier.call();
        } catch (Exception e) {
            handleException(e, false);
            return null;
        }
    }

    @Override
    public boolean catchException(@NotNull Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            handleException(e, false);
            return false;
        }
    }

    @Override
    public void handleException(@NotNull Exception exception, boolean printIfFail) {
        for (Predicate<Exception> exceptionHandler : exceptionHandlers) {
            if (exceptionHandler.test(exception)) {
                return;
            }
        }

        if (!printIfFail) throw exception instanceof RuntimeException ? (RuntimeException) exception : new RuntimeException(exception);

        exception.printStackTrace();
    }

    @Override
    public void addHandler(@NotNull Predicate<Exception> exceptionHandler) {
        this.exceptionHandlers.add(exceptionHandler);
    }
}