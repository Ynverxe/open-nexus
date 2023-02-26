package com.github.ynverxe.dtn.operation;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Operation<T> extends Supplier<T> {

    @Nullable T execute();

    Operation<T> setFailHandler(Consumer<Throwable> throwableConsumer);

    Operation<T> setFailMessage(Supplier<String> failMessageSupplier);

    Operation<T> setFailMessage(String failMessage);

    @Override
    default T get() {
        return execute();
    }
}