package com.github.ynverxe.dtn.runnable;

import com.github.ynverxe.util.Pair;
import com.github.ynverxe.util.time.Temporizer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface TemporizedRunnable extends Runnable {
    @NotNull Temporizer temporizer();

    @NotNull Pair<Long, TimeUnit> runInterval();

    boolean restartTemporizer();

    default void run() {
        final Temporizer temporizer = temporizer();
        final Pair<Long, TimeUnit> interval = runInterval();
        temporizer.elapseMillis(interval.left(), interval.right());
        if (!temporizer.isOver()) {
            return;
        }
        performRun();
        if (restartTemporizer()) {
            temporizer.restart();
        }
    }

    void performRun();
}
