package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResourceInterpreter {
    @Nullable Object buildResource(
            @NotNull ModelDataValue value,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext,
            @NotNull ResourceMapper resourceMapper
    );
}
