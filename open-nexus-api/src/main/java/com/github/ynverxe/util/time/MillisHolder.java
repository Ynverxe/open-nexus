package com.github.ynverxe.util.time;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface MillisHolder {
    @NotNull String formatMillis(@NotNull TimeUnit... p0);

    @NotNull String formatMillis();

    long millis();

    long convertMillis(@NotNull TimeUnit p0);
}
