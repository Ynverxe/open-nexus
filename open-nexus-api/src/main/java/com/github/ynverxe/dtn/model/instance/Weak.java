package com.github.ynverxe.dtn.model.instance;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public interface Weak<T> {
    boolean isAvailable();

    @NotNull Optional<T> consumeIfAvailable(@NotNull Consumer<T> p0);
}
