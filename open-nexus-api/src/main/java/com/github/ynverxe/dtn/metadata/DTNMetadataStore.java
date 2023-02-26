package com.github.ynverxe.dtn.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface DTNMetadataStore {
    @Nullable <T> T set(@NotNull String p0, @Nullable final Object p1);

    @Nullable <T> T get(@NotNull String p0);

    @NotNull <T> T get(@NotNull String p0, @NotNull T p1);

    @Nullable <T> T get(@NotNull String p0, @NotNull Class<T> p1);

    <T> void specifyValueType(@NotNull String p0, @NotNull Class<T> p1, @NotNull T p2) throws IllegalArgumentException;

    @NotNull Set<String> keys();

    @NotNull Collection<Object> values();

    boolean contains(@NotNull String p0);

    int size();

    void clear();
}
