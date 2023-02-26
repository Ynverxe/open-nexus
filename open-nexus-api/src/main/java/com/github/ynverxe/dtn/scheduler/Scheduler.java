package com.github.ynverxe.dtn.scheduler;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Scheduler {
    @NotNull static Scheduler dtnScheduler() {
        return DestroyTheNexus.instance().scheduler();
    }

    void executeTask(Runnable p0, int p1, boolean p2);

    CompletableFuture<Void> executeTaskAsFuture(Runnable p0, int p1, boolean p2);

    void executeTaskConstantly(Runnable p0, int p1, int p2, boolean p3);

    <T> CompletableFuture<T> supply(Supplier<T> p0, int p1, boolean p2);

    void cancelTask(int p0);
}
