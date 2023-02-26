package com.github.ynverxe.dtn.metadata;

import com.github.ynverxe.dtn.exception.SingletonClassException;

public final class DTNMetadataKeys {
    public static final String MATCH_NEXUS_DAMAGE = "nexus-damage-value";
    public static final String INVINCIBLE_NEXUS = "invincible-nexus";

    private DTNMetadataKeys() {
        throw new SingletonClassException(getClass());
    }
}
