package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.structured.data.ModelDataList;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StringInterpreter implements ResourceInterpreter {
    @Override
    public @Nullable Object buildResource(
            @NotNull ModelDataValue value,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext,
            @NotNull ResourceMapper resourceMapper
    ) {
        List<String> values = new ArrayList<>();

        if (value.isList()) {
            ModelDataList list = value.asList();

            list.forEach(dataValue -> values.add(dataValue.toString()));
        } else {
            values.add(value.toString());
        }

        return resourceMapper.formatStrings(values, formattingScheme, formattingContext);
    }
}