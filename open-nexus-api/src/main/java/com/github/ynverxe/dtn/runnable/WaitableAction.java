package com.github.ynverxe.dtn.runnable;

import org.jetbrains.annotations.NotNull;

public class WaitableAction implements Runnable {

    private final Runnable action;
    private final long totalWaitingMillis;
    private final long timeGoal;
    private volatile boolean executed;

    public WaitableAction(@NotNull Runnable action, long totalWaitingMillis) {
        this.action = action;
        this.totalWaitingMillis = totalWaitingMillis;
        timeGoal = System.currentTimeMillis() + totalWaitingMillis;
    }

    @Override
    public void run() {
        if (executed) {
            throw new IllegalStateException("action was been executed");
        }
        action.run();
        executed = true;
    }

    public boolean wasExecuted() {
        return executed;
    }

    public long totalWaitingMillis() {
        return totalWaitingMillis;
    }

    public long timeGoal() {
        return timeGoal;
    }
}
