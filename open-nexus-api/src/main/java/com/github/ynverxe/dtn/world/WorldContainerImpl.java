package com.github.ynverxe.dtn.world;

import com.github.ynverxe.util.metadata.MetadataHelper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldContainerImpl implements WorldContainer {

    private final List<World> worlds = new ArrayList<>();

    @Override
    public World getWorldByName(String name) {
        for (World world : worlds) {
            if (world.getName().equals(name)) return world;
        }

        throw new IllegalArgumentException("No world found with name '" + name + "'");
    }

    @Override
    public World getWorldBySchemeName(String schemeName) {
        for (World world : worlds) {
            MetadataValue found = MetadataHelper.from(world, "schemeName");

            if (found == null || !(found.value() instanceof String)) continue;

            if (found.asString().equals(schemeName)) return world;
        }

        throw new IllegalArgumentException("No world found with scheme name '" + schemeName + "'");
    }

    @Override
    public World getWorldByUUID(UUID uuid) {
        for (World world : worlds) {
            if (world.getUID().equals(uuid)) return world;
        }

        throw new IllegalArgumentException("No world found with uuid '" + uuid + "'");
    }

    @Override
    public World getWorldByIndex(int index) {
        return worlds.get(index);
    }

    @Override
    public List<World> worlds() {
        return worlds;
    }

    @Override
    public void addWorld(World world) {
        if (worlds.contains(world))
            throw new IllegalStateException("'" + world + "' was already added");

        worlds.add(world);
    }

    @Override
    public void addWorldAsSchemeRepresent(String schemeName, World world) {
        World stored = null;

        try {
            stored = getWorldBySchemeName(schemeName);
        } catch (Exception ignore) {}

        if (stored != null) {
            throw new IllegalArgumentException("'" + schemeName + "' was already registered as scheme");
        }

        MetadataValue found = MetadataHelper.from(world, "container");

        if (found != null && found.value() instanceof WorldContainer) {
            throw new IllegalStateException("'" + world.getName() + "' was already own by another container");
        }

        Plugin plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");
        world.setMetadata("schemeName", new FixedMetadataValue(plugin, schemeName));
        world.setMetadata("container", new FixedMetadataValue(plugin, this));

        addWorld(world);
    }
}