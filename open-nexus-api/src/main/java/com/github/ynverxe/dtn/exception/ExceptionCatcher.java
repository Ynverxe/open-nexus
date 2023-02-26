package com.github.ynverxe.dtn.exception;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ExceptionCatcher {

    <T> T catchException(Callable<T> supplier);

    boolean catchException(@NotNull Runnable runnable);

    void handleException(@NotNull Exception exception, boolean printIfFail);

    void addHandler(@NotNull Predicate<Exception> exceptionHandler);

    static @NotNull ExceptionCatcher instance() {
        return DestroyTheNexus.instance().exceptionCatcher();
    }

    static @NotNull <T> Function<Throwable, T> asHandler() {
        return throwable -> {
            instance().handleException((Exception) throwable, false);
            return null;
        };
    }
}