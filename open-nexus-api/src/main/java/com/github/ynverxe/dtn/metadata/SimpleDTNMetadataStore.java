package com.github.ynverxe.dtn.metadata;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDTNMetadataStore extends AbstractDTNMetadataStore {
    private final Map<String, MetadataObject> metadataMap;

    public SimpleDTNMetadataStore() {
        metadataMap = new ConcurrentHashMap<>();
    }

    @Override
    protected MetadataObject findObject(String key) {
        return metadataMap.get(key);
    }

    @Override
    protected void storeObject(String key, MetadataObject metadataObject) {
        metadataMap.put(key, metadataObject);
    }

    @NotNull @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(metadataMap.keySet());
    }

    @NotNull @Override
    public Collection<Object> values() {
        return Collections.unmodifiableCollection(metadataMap.values());
    }

    @Override
    public boolean contains(@NotNull String key) {
        return metadataMap.containsKey(key);
    }

    @Override
    public int size() {
        return metadataMap.size();
    }

    @Override
    public void clear() {
        metadataMap.clear();
    }
}
