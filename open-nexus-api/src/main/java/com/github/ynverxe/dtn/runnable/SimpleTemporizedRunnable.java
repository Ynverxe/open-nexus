package com.github.ynverxe.dtn.runnable;

import com.github.ynverxe.util.Pair;
import com.github.ynverxe.util.time.Temporizer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract class SimpleTemporizedRunnable implements TemporizedRunnable {
    private final Temporizer temporizer;
    private final Pair<Long, TimeUnit> runInterval;
    private final boolean restartTemporizer;

    protected SimpleTemporizedRunnable(Temporizer temporizer, Pair<Long, TimeUnit> runInterval, boolean restartTemporizer) {
        this.temporizer = temporizer;
        this.runInterval = runInterval;
        this.restartTemporizer = restartTemporizer;
    }

    @NotNull @Override
    public Temporizer temporizer() {
        return temporizer;
    }

    @NotNull @Override
    public Pair<Long, TimeUnit> runInterval() {
        return runInterval;
    }

    @Override
    public boolean restartTemporizer() {
        return restartTemporizer;
    }
}
