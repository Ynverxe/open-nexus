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
}