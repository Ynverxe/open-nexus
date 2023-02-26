package com.github.ynverxe.dtn.runnable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CompositeRunnable extends Runnable {
    @NotNull List<Runnable> runnableList();

    void addRunnable(@NotNull Runnable runnable, boolean ensureSync);

    void removeRunnable(@NotNull Runnable runnable);
}
