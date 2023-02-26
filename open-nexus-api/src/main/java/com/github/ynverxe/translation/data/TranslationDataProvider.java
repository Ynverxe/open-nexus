package com.github.ynverxe.translation.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unchecked, rawtypes")
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

    public @Nullable List<Map<String, Object>> findTranslationData(@NotNull String lang, @NotNull String[] path, char charSeparator) throws IllegalArgumentException {
        DataSource dataSource = cachedSources.get(lang);
        if (dataSource == null) {
            if (factory == null) {
                throw new IllegalArgumentException("No data source found with name: " + lang);
            }
            dataSource = factory.create(lang);
        }

        Object found = dataSource.findData(path, charSeparator);
        if (found instanceof List) {
            return new ArrayList<>((List) found);
        }

        if (found instanceof Map) {
            return Collections.singletonList((Map<String, Object>) found);
        }

        if (found == null) {
            return null;
        }

        return Collections.singletonList(Collections.singletonMap("value", found));
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
