package com.github.ynverxe.dtn.world;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface WorldHelper {

    World cloneWorld(String prefix, World world);

    World createEmptyWorld(String name, boolean saveInFile);

    default World createWorldIfAbsent(String name, boolean saveInFile) {
        World world = Bukkit.getWorld(name);

        if (world == null) return createEmptyWorld(name, saveInFile);

        return world;
    }

    default boolean exists(String name) {
        return Bukkit.getWorld(name) != null;
    }

    static @NotNull WorldHelper instance() {
        return DestroyTheNexus.instance().worldHelper();
    }
}