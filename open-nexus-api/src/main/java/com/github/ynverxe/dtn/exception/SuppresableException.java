package com.github.ynverxe.dtn.exception;

import com.github.ynverxe.dtn.DestroyTheNexus;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface SuppresableException {

    Predicate<Exception> HANDLER = e -> {
        if (!(e instanceof SuppresableException)) return false;

        Logger logger = DestroyTheNexus.LOGGER;

        String message = "[" + e.getClass().getSimpleName() + "] " + e.getMessage();
        logger.log(Level.WARNING, message);

        return true;
    };

    default Exception asException() {
        return this instanceof Exception ? (Exception) this : null;
    }
}