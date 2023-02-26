package com.github.ynverxe.util.cache;

import com.github.ynverxe.dtn.model.instance.DataTransferer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public interface CacheModel<K, V> {
    static @NotNull <K, V> Mutable<K, V> create() {
        return new SimpleCache<>(new HashMap<>());
    }

    static @NotNull <K, V> Mutable<K, V> create(@NotNull Map<K, V> map) {
        Objects.requireNonNull(map, "map");
        return new SimpleCache<>(map);
    }

    @Nullable V get(@NotNull K key);

    @NotNull Optional<V> safeGet(@NotNull K key);

    @NotNull Set<K> keys();

    @NotNull Collection<V> values();

    @NotNull Set<Map.Entry<K, V>> entries();

    boolean has(@NotNull K key);

    int cachedSize();

    interface Mutable<K, V> extends CacheModel<K, V>, DataTransferer<CacheModel.Mutable<K, V>> {
        @Nullable V set(@NotNull K key, @Nullable final V value);

        default @NotNull V compute(@NotNull K key, @NotNull Function<K, V> valueSupplier) {
            V found = get(key);

            if (found == null) {
                found = valueSupplier.apply(key);
                set(key, found);
            }

            return found;
        }

        default @NotNull V computeSupplying(@NotNull K key, @NotNull Supplier<V> valueSupplier) {
            return compute(key, (k) -> valueSupplier.get());
        }

        void clear();
    }
}
