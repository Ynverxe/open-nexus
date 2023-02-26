package com.github.ynverxe.translation.resource;

import com.github.ynverxe.translation.resource.mapping.FormattingScheme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractResourceReference<T> implements ResourceReference<T> {

    protected final FormattingScheme formattingScheme;
    protected final String path;
    protected final char pathSeparator;
    protected final Class<T> type;
    protected final String dispatchMode;
    protected final Supplier<Object> defaultValueSupplier;

    AbstractResourceReference(FormattingScheme formattingScheme, String path, char pathSeparator, Class<T> type, String dispatchMode, Supplier<Object> defaultValueSupplier) {
        this.formattingScheme = formattingScheme;
        this.path = path;
        this.pathSeparator = pathSeparator;
        this.type = type;
        this.dispatchMode = dispatchMode;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @NotNull @Override
    public FormattingScheme formattingScheme() {
        return formattingScheme;
    }

    @NotNull @Override
    public Class<T> type() {
        return type;
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
        return newReferenceImpl(formattingScheme, path, pathSeparator, type, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withDefaultProvider(@NotNull Supplier<Object> defaultProvider) {
        return newReferenceImpl(formattingScheme, path, pathSeparator, type, dispatchMode, defaultProvider);
    }

    @NotNull @Override
    public ResourceReference<T> withPath(@NotNull String path) {
        return newReferenceImpl(formattingScheme, path, pathSeparator, type, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withSeparator(char separator) {
        return newReferenceImpl(formattingScheme, path, separator, type, dispatchMode, defaultValueSupplier);
    }

    @NotNull @Override
    public ResourceReference<T> withDispatchMode(@NotNull String dispatchMode) {
        return newReferenceImpl(formattingScheme, path, pathSeparator, type, dispatchMode, defaultValueSupplier);
    }

    abstract ResourceReference<T> newReferenceImpl(FormattingScheme p0, String p1, char p2, Class<T> p3, String p4, Supplier<Object> p5);
}
