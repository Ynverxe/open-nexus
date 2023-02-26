package com.github.ynverxe.dtn;

public final class DTNInitializer {
    private DTNInitializer() {
        throw new UnsupportedOperationException("cannot instantiate this class");
    }
    
    public static void initialize() {
        boolean alreadyInitialized = false;
        try {
            DestroyTheNexus.instance();
            alreadyInitialized = true;
        }
        catch (Exception ex) {}
        if (alreadyInitialized) {
            throw new UnsupportedOperationException("dtn is already initialized");
        }
        DestroyTheNexusImpl.initializeAPIClass();
    }
}
