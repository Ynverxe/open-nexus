package com.github.ynverxe.util.metadata;

import org.bukkit.Bukkit;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

public final class MetadataHelper {

    private MetadataHelper() {}

    public static MetadataValue from(Metadatable metadatable, String key) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");

        List<MetadataValue> metadataValues = metadatable.getMetadata(key)
                .stream()
                .filter(value -> plugin.equals(value.getOwningPlugin()))
                .collect(Collectors.toList());

        if (metadataValues.isEmpty()) return null;

        return metadataValues.get(0);
    }

    public static MetadataValue filtering(Metadatable metadatable, String key, Class<?> expected) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");

        List<MetadataValue> metadataValues = metadatable.getMetadata(key)
                .stream()
                .filter(value -> plugin.equals(value.getOwningPlugin()))
                .filter(metadata -> expected.isInstance(metadata.value()))
                .collect(Collectors.toList());

        if (metadataValues.isEmpty()) return null;

        return metadataValues.get(0);
    }

    public static <T> T filteringAndGet(Metadatable metadatable, String key, Class<T> expected) {
        MetadataValue found = filtering(metadatable, key, expected);

        if (found == null) return null;

        return expected.cast(found.value());
    }
}