package com.github.ynverxe.translation.resource.mapping;

import org.jetbrains.annotations.NotNull;

public interface StringInterceptor {
    @NotNull String intercept(@NotNull String p0, @NotNull FormattingScheme p1, @NotNull FormattingContext p2);
}
