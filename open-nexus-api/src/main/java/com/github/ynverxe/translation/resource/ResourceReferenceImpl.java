package com.github.ynverxe.translation.resource;

import com.github.ynverxe.translation.resource.mapping.FormattingScheme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class ResourceReferenceImpl<T> implements ResourceReference<T> {

    private final FormattingScheme formattingScheme;
    private final String path;
    private final char pathSeparator;
    private final ResourceTypeDescriptor typeDescriptor;
    private final String dispatchMode;
    private final Supplier<Object> defaultValueSupplier;

    ResourceReferenceImpl(FormattingScheme formattingScheme, String path, char pathSeparator, ResourceTypeDescriptor typeDescriptor, String dispatchMode, Supplier<Object> defaultValueSupplier) {
        this.formattingScheme = formattingScheme;
        this.path = path;
        this.pathSeparator = pathSeparator;
        this.typeDescriptor = typeDescriptor;
        this.dispatchMode = dispatchMode;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @NotNull @Override
    public FormattingScheme formattingScheme() {
        return formattingScheme;
    }

    @NotNull @Override
    public String renderMode() {
        return dispatchMode;
    }

    @NotNull @Override
    public String path() {
        return path;
    }

    @Override
    public char pathSeparator() {
        return pathSeparator;
    }

    @Override
    public @NotNull ResourceTypeDescriptor typeDescriptor() {
        return typeDescriptor;
    }

    @Override
    public @Nullable List<T> defaultValue() {
        Object value = defaultValueSupplier.get();
        if (value instanceof List) {
            return (List<T>) value;
        }

        if (value != null) {
            return Collections.singletonList((T) value);
        }

        return null;
    }

    @NotNull @Override
    public ResourceReference<T> withFormattingScheme(@NotNull FormattingScheme formattingScheme) {
        return new ResourceReferenceImpl<>(formattingScheme, path, pathSeparator, typeDescriptor, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withDefaultProvider(@NotNull Supplier<Object> defaultProvider) {
        return new ResourceReferenceImpl<>(formattingScheme, path, pathSeparator, typeDescriptor, dispatchMode, defaultProvider);
    }

    @NotNull @Override
    public ResourceReference<T> withPath(@NotNull String path) {
        return new ResourceReferenceImpl<>(formattingScheme, path, pathSeparator, typeDescriptor, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withSeparator(char separator) {
        return new ResourceReferenceImpl<>(formattingScheme, path, separator, typeDescriptor, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withDispatchMode(@NotNull String dispatchMode) {
        return new ResourceReferenceImpl<>(formattingScheme, path, pathSeparator, typeDescriptor, dispatchMode, defaultValueSupplier);
    }
}
