package com.github.ynverxe.dtn.model.instance;

import com.github.ynverxe.dtn.DestroyTheNexus;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractTerminable implements Terminable {
    private final AtomicBoolean terminated;

    public AbstractTerminable() {
        terminated = new AtomicBoolean();
    }

    @Override
    public boolean terminated() {
        return terminated.get();
    }

    @Override
    public void terminate() {
        checkNotTerminated();
        try {
            preTermination();
        } catch (Exception e) {
            Logger logger = DestroyTheNexus.LOGGER;
            logger.log(Level.SEVERE, "Unexpected exception has throw while terminating instance: " + getClass(), e);
        }
        terminated.set(true);
    }

    protected abstract void preTermination();

    protected void checkNotTerminated() {
        if (terminated.get()) {
            throw new IllegalStateException("Instance is terminated");
        }
    }
}
