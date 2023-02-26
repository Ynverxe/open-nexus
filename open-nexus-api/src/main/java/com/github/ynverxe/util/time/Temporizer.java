package com.github.ynverxe.util.time;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Temporizer {
    public static final Temporizer FAKE;

    static {
        FAKE = new Fake();
    }

    protected final AtomicLong millis;
    private final Runnable action;
    private final long initialMillis;

    public Temporizer(Runnable action, long millis) {
        if (millis <= 0L) {
            throw new IllegalArgumentException("millis <= 0");
        }

        this.action = action;
        this.millis = new AtomicLong(millis);
        this.initialMillis = millis;
    }

    public Temporizer(Runnable action, long units, TimeUnit timeUnit) {
        this(action, timeUnit.toMillis(units));
    }

    public void elapseMillis(long millis) {
        setMillis(millis() - millis);
    }

    public void elapseMillis(long units, TimeUnit timeUnit) {
        final long millis = timeUnit.toMillis(units);
        elapseMillis(millis);
    }

    public long millis() {
        return millis.get();
    }

    public void setMillis(long millis) {
        this.millis.set(Math.max(millis, 0L));
        tryPerformAction();
    }

    public @NotNull MillisSnapshot toMillisSnapshot() {
        return new MillisSnapshot(millis.get());
    }

    public void restart() {
        setMillis(initialMillis);
    }

    public boolean isOver() {
        return millis.get() <= 0L;
    }

    public boolean isStarted() {
        return millis.get() < initialMillis;
    }

    private void tryPerformAction() {
        if (action != null && (int) millis() <= 0) {
            try {
                action.run();
            } catch (Exception e) {
                final Logger logger = DestroyTheNexus.LOGGER;
                logger.log(Level.SEVERE, "Unable to perform temporizer action", e);
            }
        }
    }

    private static class Fake extends Temporizer {
        public Fake() {
            super(null, 1L);
            millis.set(0L);
        }

        @Override
        public void setMillis(long millis) {
        }
    }
}
