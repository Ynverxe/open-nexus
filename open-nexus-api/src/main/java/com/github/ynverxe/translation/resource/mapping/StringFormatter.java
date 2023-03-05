package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.translation.resource.ResourceReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class StringFormatter {

    private StringFormatter() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked, rawtypes")
    public static @NotNull String format(@NotNull String str, @NotNull FormattingScheme scheme, @NotNull FormattingContext context) {
        for (Map.Entry<String, Function<FormattingContext, Object>> entry : scheme.replacements().entrySet()) {
            Object value = entry.getValue().apply(context);

            if (value instanceof ResourceReference) {
                ResourceReference resourceReference = (ResourceReference) value;

                if (resourceReference.typeDescriptor().type() == String.class) {
                    ResourceMapper resourceMapper = context.getResourceMapper();

                    if (resourceMapper != null) {
                        List<String> resourceResult = resourceMapper.formatResource(resourceReference, context);

                        if (!resourceResult.isEmpty()) {
                            value = resourceResult.get(0);
                        }
                    }
                }
            }

            str = str.replace(entry.getKey(), Objects.toString(value));
        }

        if (!scheme.nativeFormattingValues().isEmpty()) {
            str = String.format(str, scheme.nativeFormattingValues().toArray());
        }

        return str;
    }
}
