package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.util.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class FormattingScheme implements Cloneable {

    private final List<Supplier<Object>> nativeFormattingValues;
    private final Map<String, Function<FormattingContext, Object>> replacements;

    public FormattingScheme() {
        nativeFormattingValues = new ArrayList<>();
        replacements = new HashMap<>();
    }

    public @NotNull FormattingScheme addReplacements(@NotNull Object... values) {
        replacements.putAll(Maps.ofPairs(values));
        return this;
    }

    public @NotNull FormattingScheme addReplacement(@NotNull String key, @NotNull Function<FormattingContext, Object> valueSupplier) {
        replacements.put(key, valueSupplier);
        return this;
    }

    public @NotNull FormattingScheme addReplacement(@NotNull String key, @NotNull Object replacement) {
        return addReplacement(key, context -> replacement);
    }

    @SuppressWarnings("unchecked")
    public @NotNull FormattingScheme addNativeFormattingValues(@NotNull Object... values) {
        for (Object value : values) {
            Supplier<Object> supplier = value instanceof Supplier ? (Supplier<Object>) value : () -> value;
            nativeFormattingValues.add(supplier);
        }

        return this;
    }

    public @NotNull Map<String, Function<FormattingContext, Object>> replacements() {
        return replacements;
    }

    public @NotNull List<Supplier<Object>> nativeFormattingValues() {
        return nativeFormattingValues;
    }

    public @NotNull FormattingScheme consume(@NotNull FormattingScheme other) {
        replacements.putAll(other.replacements);
        return this;
    }

    public FormattingScheme clone() {
        try {
            return (FormattingScheme) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
