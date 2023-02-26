package com.github.ynverxe.util.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DelegatedCacheModel<K, V> implements CacheModel<K, V> {
    protected final Mutable<K, V> delegate;

    protected DelegatedCacheModel(Mutable<K, V> delegate) {
        this.delegate = delegate;
    }

    @NotNull @Override
    public V get(@NotNull K key) {
        return delegate.get(key);
    }

    @NotNull @Override
    public Optional<V> safeGet(@NotNull K key) {
        return delegate.safeGet(key);
    }

    @NotNull @Override
    public Set<K> keys() {
        return delegate.keys();
    }

    @NotNull @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @NotNull @Override
    public Set<Map.Entry<K, V>> entries() {
        return delegate.entries();
    }

    @Override
    public boolean has(@NotNull K key) {
        return delegate.has(key);
    }

    @Override
    public int cachedSize() {
        return delegate.cachedSize();
    }
}
