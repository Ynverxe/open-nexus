package com.github.ynverxe.dtn.gson;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.structured.data.ModelDataValue;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImplicitlyDeserializableAdapter
        implements JsonSerializer<ImplicitlyDeserializable>, JsonDeserializer<ImplicitlyDeserializable> {

    @Override
    public ImplicitlyDeserializable deserialize(
            JsonElement json, Type type, JsonDeserializationContext context
    ) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String modelAlias = jsonObject.remove(ImplicitlyDeserializable.ALIAS_KEY).getAsString();

        @SuppressWarnings("rawtypes")
        ModelDataParser parser = ImplicitlyDeserializable.PARSERS.get(modelAlias);

        if (parser == null) {
            throw new RuntimeException("'" + modelAlias + "' doesn't have a registered parser");
        }

        JsonObject modelData = jsonObject;

        if (jsonObject.has("modelData")) {
            modelData = jsonObject.getAsJsonObject("modelData");
        }

        Map<String, Object> map = context.deserialize(modelData, Map.class);

        return (ImplicitlyDeserializable) parser.parseDataValue(ModelDataValue.ensureSafety(map));
    }

    @Override
    public JsonElement serialize(
            ImplicitlyDeserializable src,
            Type typeOfSrc,
            JsonSerializationContext context
    ) {
        ModelDataValue modelData = src.toModelData();

        Map<String, Object> finalData = new LinkedHashMap<>();

        finalData.put(ImplicitlyDeserializable.ALIAS_KEY, src.alias());

        if (modelData.isTree()) {
            finalData.putAll(modelData.asTree().simplify());
        } else {
            finalData.put("modelData", modelData.trySimplifyValue());
        }

        return context.serialize(finalData);
    }
}