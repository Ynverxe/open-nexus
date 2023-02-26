package com.github.ynverxe.translation.resource.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ResourceInterpreter {
    @Nullable Object buildResource(@NotNull Map<String, Object> p0, @NotNull FormattingScheme p1, @NotNull FormattingContext p2, @NotNull ResourceMapper p3);
}
