package com.github.ynverxe.dtn.authorization;

import com.github.ynverxe.translation.resource.ResourceReference;

import java.util.Objects;

public final class Authorization {
    private final ResourceReference<String> reasonMessage;
    private final boolean authorized;

    public Authorization(ResourceReference<String> messagingResource, boolean authorized) {
        reasonMessage = Objects.requireNonNull(messagingResource, "messagingResource");
        this.authorized = authorized;
    }

    public ResourceReference<String> reasonMessage() {
        return reasonMessage;
    }

    public boolean authorized() {
        return authorized;
    }
}
