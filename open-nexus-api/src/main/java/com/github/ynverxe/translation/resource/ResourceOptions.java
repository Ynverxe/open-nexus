package com.github.ynverxe.translation.resource;

import org.jetbrains.annotations.NotNull;

public class ResourceOptions {

    private final String typeName;
    private final Class<?> type;

    public ResourceOptions(String typeName, Class<?> type) {
        this.typeName = typeName;
        this.type = type;
    }

    public @NotNull Class<?> type() {
        return type;
    }

    public @NotNull String typeName() {
        return typeName;
    }
}
