package com.github.ynverxe.dtn.storage;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public class GsonMapper<T> implements PlainTextModelRepository.Mapper<T> {

    private final Gson gson;
    private final Class<T> clazz;

    public GsonMapper(Gson gson, Class<T> clazz) {
        this.gson = gson;
        this.clazz = clazz;
    }

    @NotNull @Override
    public T consumeData(@NotNull String data) {
        return gson.fromJson(data, clazz);
    }

    @NotNull @Override
    public String serializeModel(@NotNull T model) {
        return gson.toJson(model);
    }

}
