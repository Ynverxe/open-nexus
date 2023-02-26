package com.github.ynverxe.dtn.storage;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PlainTextModelRepositoryImpl<T> implements PlainTextModelRepository<T> {

    private final Mapper<T> modelMapper;
    private final DatabaseHandler databaseHandler;

    PlainTextModelRepositoryImpl(Mapper<T> modelMapper, DatabaseHandler databaseHandler) {
        this.modelMapper = modelMapper;
        this.databaseHandler = databaseHandler;
    }

    @NotNull @Override
    public Optional<String> findModelData(@NotNull String key) {
        String modelText = databaseHandler.findModelText(key);

        if (modelText == null) {
            return Optional.empty();
        }

        return Optional.of(modelText);
    }

    @NotNull @Override
    public Optional<T> find(@NotNull String key) {
        return findModelData(key).map((Function<? super String, ? extends T>) modelMapper::consumeData);
    }

    @NotNull @Override
    public String save(@NotNull String key, @NotNull T model) {
        final String modelData = modelMapper.serializeModel(model);
        databaseHandler.saveModelText(key, modelData);
        return modelData;
    }

    @Override
    public void delete(@NotNull String key) {
        databaseHandler.deleteModelText(key);
    }

    @NotNull @Override
    public Map<String, String> groupAllModelData() {
        return databaseHandler.allModels();
    }

    @NotNull @Override
    public Map<String, T> groupAll() {
        return databaseHandler.allModels()
                .entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), modelMapper.consumeData(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
