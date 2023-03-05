package com.github.ynverxe.translation.data;

import com.github.ynverxe.structured.data.ModelDataList;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TranslationDataProvider {

    private final DataSource.Factory factory;
    private final Map<String, DataSource> cachedSources;

    public TranslationDataProvider() {
        cachedSources = new HashMap<>();
        factory = null;
    }

    public TranslationDataProvider(DataSource.Factory factory) {
        cachedSources = new HashMap<>();
        this.factory = factory;
    }

    public @Nullable ModelDataList findTranslationData(
            @NotNull String lang,
            @NotNull String[] path,
            char charSeparator
    ) throws IllegalArgumentException {
        DataSource dataSource = cachedSources.get(lang);
        if (dataSource == null) {
            if (factory == null) {
                throw new IllegalArgumentException("No data source found with name: " + lang);
            }
            dataSource = factory.create(lang);
        }

        ModelDataValue found = dataSource.findData(path, charSeparator);

        if (found == null) return null;

        if (found.isList()) {
            return found.asList();
        }

        ModelDataList list = new ModelDataList();
        list.add(found);

        return list;
    }

    public @NotNull TranslationDataProvider addSource(@NotNull String lang, @NotNull DataSource dataSource) {
        cachedSources.put(lang, dataSource);
        return this;
    }

    public @NotNull DataSource getSource(@NotNull String lang) {
        return cachedSources.get(lang);
    }

    public void invalidateSources() {
        cachedSources.clear();
    }
}
