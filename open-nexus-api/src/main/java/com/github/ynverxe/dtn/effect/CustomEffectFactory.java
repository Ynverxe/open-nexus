package com.github.ynverxe.dtn.effect;

import org.jetbrains.annotations.NotNull;

public interface CustomEffectFactory<T> {
    @NotNull CustomEffect create(@NotNull T p0);
}
