package com.github.ynverxe.dtn;

import com.github.ynverxe.dtn.exception.SingletonClassException;

public final class DTNSingletonContainer {
    private static volatile DestroyTheNexus INSTANCE;

    public static void setInstance(DestroyTheNexus instance) {
        if (instance == null) {
            throw new NullPointerException("null instance");
        }
        if (DTNSingletonContainer.INSTANCE == null) {
            synchronized (DTNSingletonContainer.class) {
                if (DTNSingletonContainer.INSTANCE == null) {
                    DTNSingletonContainer.INSTANCE = instance;
                    return;
                }
            }
        }
        throw new SingletonClassException(DTNSingletonContainer.class);
    }

    public static DestroyTheNexus dtnInstance() {
        return DTNSingletonContainer.INSTANCE;
    }
}
