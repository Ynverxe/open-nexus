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

    char DEFAULT_PATH_SEPARATOR = '.';

    @NotNull FormattingScheme formattingScheme();

    @NotNull String renderMode();

    @NotNull String path();

    char pathSeparator();

    @Nullable List<T> defaultValue();

    @NotNull ResourceTypeDescriptor typeDescriptor();

    @NotNull ResourceReference<T> withFormattingScheme(@NotNull FormattingScheme p0);

    @NotNull ResourceReference<T> withDefaultProvider(@NotNull Supplier<Object> p0);

    @NotNull ResourceReference<T> withPath(@NotNull String p0);

    @NotNull ResourceReference<T> withSeparator(char separator);

    @NotNull ResourceReference<T> withDispatchMode(@NotNull String p0);

    default @NotNull Optional<List<T>> optionalDefaultValue() {
        return Optional.ofNullable(defaultValue());
    }

    default boolean isEmpty() {
        return !optionalDefaultValue().isPresent() && path().isEmpty();
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

    static @NotNull <T> ResourceReference<T> create(@NotNull Class<T> type, @NotNull String path, char pathSeparator) {
        return new ResourceReferenceImpl<>(
                new FormattingScheme(),
                path,
                pathSeparator,
                new ResourceTypeDescriptor(type),
                "default",
                () -> null
        );
    }

    static @NotNull <T> ResourceReference<T> create(@NotNull Class<T> type, @NotNull String path) {
        return create(type, path, DEFAULT_PATH_SEPARATOR);
    }

    static @NotNull ResourceReferenceImpl<?> create(@NotNull String typeName, @NotNull String path, char pathSeparator) {
        return new ResourceReferenceImpl<>(
                new FormattingScheme(),
                path,
                pathSeparator,
                new ResourceTypeDescriptor(typeName),
                "default",
                () -> null
        );
    }

    static @NotNull ResourceReferenceImpl<?> create(@NotNull String typeName, @NotNull String path) {
        return create(typeName, path, DEFAULT_PATH_SEPARATOR);
    }

    static @NotNull <T> ResourceReference<T> createWithAuxiliary(@NotNull Class<T> type, @NotNull Supplier<Object> defaultValueSupplier) {
        return create(type, "").withDefaultProvider(defaultValueSupplier);
    }

    static @NotNull <T> ResourceReference<T> createWithAuxiliary(@NotNull T value) {
        return createWithAuxiliary((Class<T>) value.getClass(), () -> value);
    }

    static @NotNull <T> ResourceReference<T> resolve(Object value) {
        if (value instanceof ResourceReference) {
            return (ResourceReference<T>) value;
        }
        return (ResourceReference<T>) createWithAuxiliary(value);
    }
}
