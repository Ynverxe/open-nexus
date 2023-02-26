package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.translation.data.TranslationDataProvider;
import com.github.ynverxe.translation.resource.ResourceOptions;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ResourceMapper {

    static @NotNull ResourceMapper create(@NotNull TranslationDataProvider translationDataProvider) {
        return new ResourceMapperImpl(translationDataProvider);
    }

    @NotNull List<String> formatStrings(
            @NotNull List<String> strings,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext
    );

    @NotNull <T> List<T> formatData(
            @NotNull Map<String, Object> data,
            @NotNull ResourceOptions resourceOptions,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext
    );

    @NotNull <T> List<T> formatResource(
            @NotNull ResourceReference<T> resourceReference,
            @NotNull FormattingContext formattingContext
    );

    @NotNull <T> ResourceMapper registerInterpreter(@NotNull Class<T> interpreterClass, @NotNull ResourceInterpreter resourceInterpreter);

    void registerStringInterceptors(@NotNull StringInterceptor... stringInterceptors);

    @NotNull ResourceMapper bindClassTypeName(@NotNull String classTypeName, @NotNull Class<?> clazz);

    @NotNull TranslationDataProvider translationDataProvider();
}
