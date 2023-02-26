package com.github.ynverxe.util.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleCache<K, V> implements CacheModel.Mutable<K, V> {
    private final Map<K, V> map;

    protected SimpleCache(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return map.get(key);
    }

    @Override
    public @NotNull Optional<V> safeGet(@NotNull K key) {
        return Optional.ofNullable(get(key));
    }

    @Override
    public @NotNull Set<K> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @NotNull @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public @NotNull Set<Map.Entry<K, V>> entries() {
        return Collections.unmodifiableSet(map.entrySet());
    }

    @Override
    public boolean has(@NotNull K key) {
        return map.containsKey(key);
    }

    @Override
    public int cachedSize() {
        return map.size();
    }

    @Override
    public @Nullable V set(@NotNull K key, @Nullable final V value) {
        return map.put(Objects.requireNonNull(key, "key"), value);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void shareData(Mutable<K, V> recipient) {
        map.forEach(recipient::set);
    }
}
