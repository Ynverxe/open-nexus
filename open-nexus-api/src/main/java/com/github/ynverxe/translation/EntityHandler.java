package com.github.ynverxe.translation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityHandler<T> {
    default @NotNull String getLang(@NotNull T entity) {
        return "en";
    }

    default boolean displayMessage(@NotNull T entity, @NotNull Object message, @NotNull String mode, @NotNull Class<?> messageClass) {
        return false;
    }

    @Nullable Object transformEntity(@NotNull T p0);

    @NotNull Class<T> representingType();
}
