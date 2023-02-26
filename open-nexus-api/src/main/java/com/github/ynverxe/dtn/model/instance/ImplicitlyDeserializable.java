package com.github.ynverxe.dtn.model.instance;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.structured.data.ModelDataHolder;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an object that doesn't have an explicit reference
 * on the code, but leaves its mark on his serialized data.
 * In this form, is easier recognize it during deserialization
 * runtime.
 */
@SuppressWarnings("unchecked, rawtypes")
public interface ImplicitlyDeserializable extends ModelDataHolder {

    String ALIAS_KEY = "modelTypeReference";

    ModelDataParser PARSER = new ModelDataParser() {
        @Override
        public Object parseDataValue(ModelDataValue modelDataValue) {
            ModelDataTree tree = modelDataValue.asTree();

            String modelAlias = tree.getValue(ALIAS_KEY).asString();

            ModelDataParser parser = PARSERS.get(modelAlias);

            if (parser == null)
                throw new RuntimeException("No ModelDataValueParser found for model alias '" + modelAlias + "'");

            return parser.parseDataValue(modelDataValue);
        }
    };

    Map<String, ModelDataParser<?>> PARSERS = new HashMap<>();

    default String alias() {
        return getClass().getName();
    }

    static <T> T expectModel(@NotNull ModelDataValue value) {
        Object toReturn;

        if (value.isList()) {
            toReturn = PARSER.toList()
                    .parseDataValue(value);
        } else {
            toReturn = PARSER.parseDataValue(value);
        }

        return (T) toReturn;
    }

    static <T> T expectModel(
            @NotNull ModelDataValue value,
            ModelDataParser<T> defParser
    ) {
        Object toReturn;

        try {
            if (value.isList()) {
                toReturn = PARSER.toList()
                        .parseDataValue(value);
            } else {
                toReturn = PARSER.parseDataValue(value);
            }
        } catch (Exception e) {
            if (value.isList()) {
                toReturn = defParser.toList()
                        .parseDataValue(value);
            } else {
                toReturn = defParser.parseDataValue(value);
            }
        }

        return (T) toReturn;
    }
}