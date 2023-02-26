package com.github.ynverxe.translation.resource.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class StringInterpreter implements ResourceInterpreter {
    @Override
    public @Nullable Object buildResource(@NotNull Map<String, Object> data, @NotNull FormattingScheme formattingScheme, @NotNull FormattingContext formattingContext, @NotNull ResourceMapper resourceMapper) {
        String str = String.valueOf(data.values().toArray(new Object[] {})[0]);
        return resourceMapper.formatStrings(Collections.singletonList(str), formattingScheme, formattingContext);
    }
}
