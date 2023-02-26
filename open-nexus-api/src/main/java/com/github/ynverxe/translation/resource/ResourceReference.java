package com.github.ynverxe.translation.resource;

import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.translation.resource.mapping.FormattingScheme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface ResourceReference<T> {

    static @NotNull <T> ResourceReference<T> create(@NotNull Class<T> type, @NotNull String path) {
        return new TypedResourceReference<>(new FormattingScheme(), path, '.', type, "default", () -> null);
    }

    static @NotNull <T> ResourceReference<T> create(@NotNull Class<T> type, @NotNull Supplier<Object> defaultValueSupplier) {
        return create(type, "").withDefaultProvider(defaultValueSupplier);
    }

    static @NotNull <T> ResourceReference<T> simple(@NotNull T value) {
        return create((Class<T>) value.getClass(), () -> value);
    }

    static @NotNull <T> ResourceReference<T> resolve(Object value) {
        if (value instanceof ResourceReference) {
            return (ResourceReference<T>) value;
        }
        return (ResourceReference<T>) simple(value);
    }

    static @NotNull AbstractResourceReference<?> onlyPath(@NotNull String typeName, @NotNull String path, char pathSeparator) {
        return new AbstractTypedResourceReference(new FormattingScheme(), path, pathSeparator, "default", () -> null, typeName);
    }

    static @NotNull AbstractResourceReference<?> onlyPath(@NotNull String typeName, @NotNull String path) {
        return onlyPath(typeName, path, '.');
    }

    default @NotNull Optional<List<T>> optionalDefaultValue() {
        return Optional.ofNullable(defaultValue());
    }

    default boolean isEmpty() {
        return optionalDefaultValue().isPresent() || !path().isEmpty();
    }

    default @NotNull ResourceReference<T> replacing(@NotNull String key, @NotNull Function<FormattingContext, Object> valueSupplier) {
        FormattingScheme scheme = formattingScheme().clone().addReplacement(key, valueSupplier);
        return withFormattingScheme(scheme);
    }

    default @NotNull ResourceReference<T> replacing(@NotNull String key, @NotNull Object value) {
        return replacing(key, context -> value);
    }

    default @NotNull ResourceReference<T> withDefaultValue(@NotNull Object def) {
        return withDefaultProvider(() -> def);
    }

    default boolean isAbstract() {
        return this instanceof AbstractTypedResourceReference;
    }

    @NotNull FormattingScheme formattingScheme();

    @NotNull Class<T> type();

    @NotNull String renderMode();

    @NotNull String path();

    char pathSeparator();

    @Nullable List<T> defaultValue();

    default @NotNull ResourceOptions createOptions() {
        return new ResourceOptions(null, type());
    }

    @NotNull ResourceReference<T> withFormattingScheme(@NotNull FormattingScheme p0);

    @NotNull ResourceReference<T> withDefaultProvider(@NotNull Supplier<Object> p0);

    @NotNull ResourceReference<T> withPath(@NotNull String p0);

    @NotNull ResourceReference<T> withSeparator(char p0);

    @NotNull ResourceReference<T> withDispatchMode(@NotNull String p0);
}
