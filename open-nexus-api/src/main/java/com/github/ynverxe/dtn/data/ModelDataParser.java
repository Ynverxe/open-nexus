package com.github.ynverxe.dtn.data;

import com.github.ynverxe.structured.data.ModelDataValue;

import java.util.List;
import java.util.stream.Collectors;

public interface ModelDataParser<T> {

    T parseDataValue(ModelDataValue value);

    default ModelDataParser<List<T>> toList() {
        return value -> value.asList()
                .stream()
                .map(ModelDataParser.this::parseDataValue)
                .collect(Collectors.toList());
    }
}