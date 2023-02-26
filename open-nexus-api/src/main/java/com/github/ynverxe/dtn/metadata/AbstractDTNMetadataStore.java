package com.github.ynverxe.dtn.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public abstract class AbstractDTNMetadataStore implements DTNMetadataStore {

    @Override
    public @NotNull <T> T set(@NotNull String key, @Nullable final Object value) {
        MetadataObject found = findObject(key);
        if (found != null) {
            Class<?> metadataValueType = found.type;
            if (value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }
            if (metadataValueType != null && !metadataValueType.isInstance(value)) {
                throw new IllegalArgumentException(value.getClass() + " must be an instance of: " + metadataValueType);
            }
        } else {
            found = new MetadataObject(null, null);
        }

        Object previous = found.value;
        found.value = value;
        return (T) previous;
    }

    @Override
    public <T> @Nullable T get(@NotNull String key) {
        final MetadataObject found = findObject(key);
        return (T) ((found != null) ? found.value : null);
    }

    @NotNull @Override
    public <T> T get(@NotNull String key, @NotNull T def) {
        T value = get(key);
        return (value != null) ? value : def;
    }

    @Override
    public <T> @Nullable T get(@NotNull String key, @NotNull Class<T> clazz) {
        return clazz.cast(get(key));
    }

    @Override
    public <T> void specifyValueType(@NotNull String key, @NotNull Class<T> type, @NotNull T value) throws IllegalArgumentException {
        MetadataObject found = findObject(key);
        if (found == null) {
            found = new MetadataObject(type, value);
            storeObject(key, found);
            return;
        }

        if (found.type != null) {
            throw new IllegalArgumentException("metadata entry at '" + key + "' already has a specific value type");
        }

        found.type = type;
    }

    protected abstract MetadataObject findObject(String p0);

    protected abstract void storeObject(String p0, MetadataObject p1);

    public static final class MetadataObject {
        private volatile Class<?> type;
        private volatile Object value;

        public MetadataObject(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Class<?> type() {
            return type;
        }

        public Object value() {
            return value;
        }
    }
}
