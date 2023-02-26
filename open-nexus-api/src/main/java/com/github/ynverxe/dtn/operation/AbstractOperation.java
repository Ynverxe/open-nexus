package com.github.ynverxe.dtn.operation;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractOperation<T> implements Operation<T> {

    private Consumer<Throwable> failHandler;
    private final boolean catchException;

    public AbstractOperation() {
        this(true);
    }

    protected AbstractOperation(boolean catchException) {
        this.catchException = catchException;
    }

    @Override
    public @Nullable T execute() {
        try {
            return run();
        } catch (Throwable e) {
            if (failHandler != null) failHandler.accept(e);

            if (!catchException) throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);

            ExceptionCatcher.instance().handleException((Exception) e, false);

            return null;
        }
    }

    @Override
    public Operation<T> setFailHandler(Consumer<Throwable> throwableConsumer) {
        this.failHandler = throwableConsumer;
        return this;
    }

    @Override
    public Operation<T> setFailMessage(String failMessage) {
        return setFailMessage(() -> failMessage);
    }

    @Override
    public Operation<T> setFailMessage(Supplier<String> failMessageSupplier) {
        return setFailHandler(throwable -> DestroyTheNexus.LOGGER.warning(failMessageSupplier.get()));
    }

    protected abstract T run() throws Throwable;
}