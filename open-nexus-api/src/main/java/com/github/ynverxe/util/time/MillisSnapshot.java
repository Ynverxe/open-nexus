package com.github.ynverxe.util.time;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MillisSnapshot implements MillisHolder {

    private final long millis;

    public MillisSnapshot(long millis) {
        if (millis < 0L) {
            throw new IllegalArgumentException("millis < 0");
        }
        this.millis = millis;
    }

    @NotNull @Override
    public String formatMillis(@NotNull TimeUnit... timeParts) {
        return TimeFormatter.formatTime(TimeUnit.MILLISECONDS, millis, ':', timeParts);
    }

    @NotNull @Override
    public String formatMillis() {
        return formatMillis(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS);
    }

    @Override
    public long millis() {
        return millis;
    }

    @Override
    public long convertMillis(@NotNull TimeUnit timeUnit) {
        return TimeUnit.MICROSECONDS.convert(millis, timeUnit);
    }
}
