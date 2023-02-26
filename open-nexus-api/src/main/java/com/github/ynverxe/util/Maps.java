package com.github.ynverxe.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes, unchecked")
public final class Maps {
    private Maps() {
        throw new UnsupportedOperationException();
    }

    public static @NotNull <K, V> Map<K, V> ofPairs(@NotNull Object... objects) {
        return ofPairs(Pair.fromObjects(objects));
    }

    public static @NotNull <K, V> Map<K, V> ofPairs(@NotNull List<Pair<K, V>> pairs) {
        return pairs.stream().map(pair -> new AbstractMap.SimpleEntry<>(pair.left(), pair.right()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <V> V hierarchySearch(Class clazz, Map<Class, V> map) {
        V value;
        for (value = (V) findByInterfaces(clazz, (Map<Class, Object>) map); clazz != null && value == null; clazz = clazz.getSuperclass()) {
            value = map.get(clazz);
            if (value == null) {
                value = (V) findByInterfaces(clazz, (Map<Class, Object>) map);
            }
        }
        return value;
    }

    private static <V> V findByInterfaces(Class clazz, Map<Class, V> map) {
        V value = null;
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length && value == null; ++i) {
            final Class anInterface = interfaces[i];
            value = map.get(anInterface);
            if (value == null) {
                value = (V) findByInterfaces(anInterface, (Map<Class, Object>) map);
            }
        }
        return value;
    }
}
