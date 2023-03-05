package com.github.ynverxe.translation.resource;

import org.jetbrains.annotations.NotNull;

public class ResourceOptions {

    private final @NotNull String typeName;
    private final @NotNull Class<?> type;

    public ResourceOptions(@NotNull String typeName, @NotNull Class<?> type) {
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
