package com.github.ynverxe.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public class TimestampedValue<T> {

    private final Duration stampDuration;
    private T value;
    private long lastStamp;

    public TimestampedValue(@NotNull Duration stampDuration) {
        this.stampDuration = Objects.requireNonNull(stampDuration, "stampDuration");
    }

    public @NotNull Optional<T> value() {
        final long current = System.currentTimeMillis();
        if (lastStamp - current <= 0L) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public @NotNull Optional<T> valueWithoutUpdate() {
        return Optional.ofNullable(value);
    }

    public void setValue(@NotNull T value) {
        this.value = Objects.requireNonNull(value, "value");
        lastStamp = System.currentTimeMillis() + stampDuration.toMillis();
    }
}
