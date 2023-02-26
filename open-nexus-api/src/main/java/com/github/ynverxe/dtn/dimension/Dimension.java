package com.github.ynverxe.dtn.dimension;

import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.dtn.world.WorldContainer;
import com.github.ynverxe.dtn.world.WorldContainerImpl;
import com.github.ynverxe.dtn.world.WorldHelper;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface Dimension extends Terminable {

    @NotNull String name();

    @NotNull WorldContainer worldContainer();

    @NotNull <T extends PropertiesContainer> T properties();

    @NotNull String typeName();

    @NotNull Date creationDate();

    void checkIsOfType(@NotNull String p0);

    default WorldContainer cloneWorlds(@NotNull String worldsPrefix) {
        WorldContainer worldContainer = new WorldContainerImpl();

        for (World world : worldContainer().worlds()) {
            World cloned = WorldHelper.instance().cloneWorld(worldsPrefix, world);
            worldContainer.addWorldAsSchemeRepresent(world.getName(), cloned);
        }

        return worldContainer;
    }

    static @NotNull DimensionManager manager() {
        return DimensionManager.instance();
    }
}
