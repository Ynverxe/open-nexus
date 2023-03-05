package com.github.ynverxe.translation.resource;

import org.jetbrains.annotations.NotNull;

public class ResourceTypeDescriptor {

    private final @NotNull String typeName;
    private final @NotNull Class<?> type;

    public ResourceTypeDescriptor(@NotNull Class<?> type) {
        this.type = type;
        this.typeName = "";
    }

    public ResourceTypeDescriptor(@NotNull String typeName) {
        if (typeName.isEmpty())
            throw new IllegalArgumentException("Empty typeName");

        this.typeName = typeName;
        this.type = Object.class;
    }

    public @NotNull Class<?> type() {
        return type;
    }

    public @NotNull String typeName() {
        return typeName;
    }

    public boolean isAbstract() {
        return Object.class == type;
    }
}
