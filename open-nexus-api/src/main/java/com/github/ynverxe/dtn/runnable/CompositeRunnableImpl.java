package com.github.ynverxe.dtn.runnable;

import com.github.ynverxe.dtn.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CompositeRunnableImpl implements CompositeRunnable {
    private final List<Runnable> runnableList;

    public CompositeRunnableImpl() {
        runnableList = new ArrayList<>();
    }

    @NotNull @Override
    public List<Runnable> runnableList() {
        return runnableList;
    }

    @Override
    public void addRunnable(@NotNull Runnable runnable, boolean ensureSync) {
        if (ensureSync) {
            Runnable handler = runnable;
            runnable = () -> Scheduler.dtnScheduler().executeTask(handler, 0, false);
        };

        runnableList.add(runnable);
    }

    @Override
    public void removeRunnable(@NotNull Runnable runnable) {
        runnableList.remove(runnable);
    }

    @Override
    public void run() {
        for (Runnable runnable : runnableList) {
            runnable.run();
        }
    }
}
