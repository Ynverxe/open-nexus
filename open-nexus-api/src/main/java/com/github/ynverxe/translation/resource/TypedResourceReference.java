package com.github.ynverxe.translation.resource;

import com.github.ynverxe.translation.resource.mapping.FormattingScheme;

import java.util.function.Supplier;

public class TypedResourceReference<T> extends AbstractResourceReference<T> {

    TypedResourceReference(FormattingScheme formattingScheme, String path, char pathSeparator, Class<T> type, String dispatchMode, Supplier<Object> defaultValueSupplier) {
        super(formattingScheme, path, pathSeparator, type, dispatchMode, defaultValueSupplier);
    }

    @Override
    ResourceReference<T> newReferenceImpl(FormattingScheme formattingScheme, String path, char pathSeparator, Class<T> type, String dispatchMode, Supplier<Object> defaultValueSupplier) {
        return new TypedResourceReference<>(formattingScheme, path, pathSeparator, type, dispatchMode, defaultValueSupplier);
    }
}
