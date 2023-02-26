package com.github.ynverxe.dtn.gson;

import com.github.ynverxe.util.cache.CacheModel;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked, rawtypes")
public class CacheModelAdapter implements JsonSerializer<CacheModel>, JsonDeserializer<CacheModel> {
    @Override
    public CacheModel deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return CacheModel.create(context.deserialize(element, Map.class));
    }

    @Override
    public JsonElement serialize(CacheModel cacheModel, Type type, JsonSerializationContext context) {
        Stream<Map.Entry> stream = cacheModel.entries().stream();
        return context.serialize(stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}
