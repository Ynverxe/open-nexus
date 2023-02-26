package com.github.ynverxe.dtn.gson;

import com.github.ynverxe.dtn.exception.SingletonClassException;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.util.cache.CacheModel;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class GlobalGson {
    public static final Gson BASE;

    static {
        BASE = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(CacheModel.class, new CacheModelAdapter())
                .registerTypeHierarchyAdapter(CacheModel.Mutable.class, new CacheModelAdapter())
                .registerTypeHierarchyAdapter(ImplicitlyDeserializable.class, new ImplicitlyDeserializableAdapter())
                .create();
    }

    private GlobalGson() {
        throw new SingletonClassException(getClass());
    }

    @SuppressWarnings("rawtypes")
    public static <T> T fromMap(@NotNull Map objectMap, @NotNull Class<T> expect) {
        JsonElement serialized = BASE.toJsonTree(objectMap);

        return BASE.fromJson(serialized, expect);
    }
}
