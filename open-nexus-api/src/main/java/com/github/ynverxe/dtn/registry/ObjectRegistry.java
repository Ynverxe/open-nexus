package com.github.ynverxe.dtn.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

public final class ObjectRegistry<T extends Registrable> {

    private final String name;
    private final Logger logger;

    private final Map<String, T> objectMap = new HashMap<>();

    public ObjectRegistry(String name) {
        this.name = name;
        this.logger = Logger.getLogger(name);
    }

    public void registerObject(@NotNull T registrable) {
        String id = registrable.id();
        if (objectMap.containsKey(id)) {
            throw new IllegalArgumentException("'" + id + "' belongs to another object.");
        }

        objectMap.put(id, registrable);
    }

    public @Nullable T get(@NotNull String id) {
        return objectMap.get(id);
    }

    public @NotNull T getOrSupply(String id, @NotNull Supplier<T> supplier, boolean debug) {
        T found = get(id);

        if (found == null) {
            if (debug) logger.warning("Missing registrable object: '" + id + "'");

            return supplier.get();
        }

        return found;
    }

    public @NotNull Set<String> keys() {
        return Collections.unmodifiableSet(objectMap.keySet());
    }

    public @NotNull Collection<T> values() {
        return Collections.unmodifiableCollection(objectMap.values());
    }

    public @NotNull String name() {
        return name;
    }

    @SafeVarargs
    public static <T extends Registrable> ObjectRegistry<T> create(@NotNull String name, @NotNull T... objects) {
        ObjectRegistry<T> objectRegistry = new ObjectRegistry<>(name);

        for (T object : objects) {
            objectRegistry.registerObject(object);
        }

        return objectRegistry;
    }
}