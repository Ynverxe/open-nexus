package com.github.ynverxe.dtn.world;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public interface WorldContainer extends Iterable<World> {

    World getWorldBySchemeName(String schemeName);

    World getWorldByName(String name);

    World getWorldByUUID(UUID uuid);

    World getWorldByIndex(int index);

    List<World> worlds();

    void addWorld(World world);

    void addWorldAsSchemeRepresent(String schemeName, World world);

    @Override
    default @NotNull Iterator<World> iterator() {
        return worlds().iterator();
    }
}