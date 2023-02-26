package com.github.ynverxe.translation.resource.mapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FormattingContext {

    public static final String MESSAGE_RECEPTOR_KEY = "MESSAGE_RECEPTOR";
    public static final String SOURCE_NAME_KEY = "SOURCE_NAME";
    public static final String RESOURCE_MAPPER_KEY = "RESOURCE_MAPPER";
    private final Map<String, Object> values;

    public FormattingContext() {
        values = new HashMap<>();
    }

    public @NotNull <T> T get(@NotNull String key, @NotNull Class<T> clazz) {
        return clazz.cast(values.get(key));
    }

    public void set(@NotNull String key, Object value) {
        values.put(key, value);
    }

    public @Nullable <T> T getReceptor(@NotNull Class<T> clazz) {
        return get(MESSAGE_RECEPTOR_KEY, clazz);
    }

    public void setReceptor(@Nullable Object receptor) {
        set(MESSAGE_RECEPTOR_KEY, receptor);
    }

    public @Nullable String getSourceName() {
        return get(SOURCE_NAME_KEY, String.class);
    }

    public void setSourceName(String sourceName) {
        set(SOURCE_NAME_KEY, sourceName);
    }

    public @Nullable ResourceMapper getResourceMapper() {
        return get(RESOURCE_MAPPER_KEY, ResourceMapper.class);
    }

    public void setResourceMapper(ResourceMapper resourceMapper) {
        set(RESOURCE_MAPPER_KEY, resourceMapper);
    }

    public @NotNull Map<String, Object> values() {
        return values;
    }
}
