package com.github.ynverxe.translation.resource;

import com.github.ynverxe.translation.resource.mapping.FormattingScheme;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AbstractTypedResourceReference extends AbstractResourceReference<Object> {
    private final @NotNull String typeName;

    AbstractTypedResourceReference(FormattingScheme formattingScheme, String path, char pathSeparator, String dispatchMode, Supplier<Object> defaultValueSupplier, @NotNull String typeName) {
        super(formattingScheme, path, pathSeparator, Object.class, dispatchMode, defaultValueSupplier);
        this.typeName = typeName;
    }

    @Override
    AbstractTypedResourceReference newReferenceImpl(FormattingScheme formattingScheme, String path, char pathSeparator, Class<Object> type, String dispatchMode, Supplier<Object> defaultValueSupplier) {
        return new AbstractTypedResourceReference(formattingScheme, path, pathSeparator, dispatchMode, defaultValueSupplier, typeName);
    }

    @NotNull @Override
    public AbstractTypedResourceReference withFormattingScheme(@NotNull FormattingScheme formattingScheme) {
        return (AbstractTypedResourceReference) super.withFormattingScheme(formattingScheme);
    }

    @NotNull @Override
    public AbstractTypedResourceReference withDefaultProvider(@NotNull Supplier<Object> defaultProvider) {
        return (AbstractTypedResourceReference) super.withDefaultProvider(defaultProvider);
    }

    @NotNull @Override
    public AbstractTypedResourceReference withPath(@NotNull String path) {
        return (AbstractTypedResourceReference) super.withPath(path);
    }

    @NotNull @Override
    public AbstractTypedResourceReference withSeparator(char separator) {
        return (AbstractTypedResourceReference) super.withSeparator(separator);
    }

    @NotNull @Override
    public AbstractTypedResourceReference withDispatchMode(@NotNull String dispatchMode) {
        return (AbstractTypedResourceReference) super.withDispatchMode(dispatchMode);
    }

    public @NotNull String typeName() {
        return typeName;
    }

    @NotNull @Override
    public ResourceOptions createOptions() {
        return new ResourceOptions(typeName, Object.class);
    }

    public @NotNull AbstractTypedResourceReference withTypeName(@NotNull String typeName) {
        return new AbstractTypedResourceReference(formattingScheme, path, pathSeparator, dispatchMode, defaultValueSupplier, typeName);
    }
}
