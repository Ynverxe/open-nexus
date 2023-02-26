package com.github.ynverxe.dtn.match;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public interface TieBreaker {
    @Nullable Duration runTieBreak(@NotNull Match match);
}