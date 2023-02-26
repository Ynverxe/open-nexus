package com.github.ynverxe.translation.resource.mapping;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface InternalFormatting {
    void acceptFormatter(@NotNull Function<String, String> p0);
}
