package com.github.ynverxe.dtn.world;

import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.nms.SlimeNMS;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WorldHelperImpl implements WorldHelper {

    private final Plugin plugin;
    private final SWMPlugin swmPlugin;
    private final SlimeNMS slimeNMS;
    private final SlimeLoader slimeLoader;

    public WorldHelperImpl() {
        this.swmPlugin = SWMPlugin.getInstance();
        this.slimeNMS = swmPlugin.getNms();
        this.slimeLoader = swmPlugin.getLoader("file");
        this.plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");
    }

    @Override
    public World cloneWorld(String prefix, World world) {
        SlimeWorld slimeWorld = this.slimeNMS.getSlimeWorld(world);

        if (slimeWorld == null)
            throw new IllegalArgumentException(world.getName() + " is not a swm world");

        String newName = prefix + world.getName();

        World found = Bukkit.getWorld(newName);

        if (found != null) return found;

        SlimeWorld cloned = slimeWorld.clone(newName);
        return generateWorld(cloned, false);
    }

    @Override
    public World createEmptyWorld(String name, boolean saveInFile) {
        try {
            SlimeWorld slimeWorld = swmPlugin.createEmptyWorld(slimeLoader, name, false, new SlimePropertyMap());
            return generateWorld(slimeWorld, saveInFile);
        } catch (WorldAlreadyExistsException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private World generateWorld(SlimeWorld slimeWorld, boolean saveInFile) {
        CompletableFuture<World> future = new CompletableFuture<>();
        Runnable runnable = () -> {
            swmPlugin.generateWorld(slimeWorld);
            WorldData worldData = new WorldData();
            worldData.setSpawn("0, 64, 0");
            String name = slimeWorld.getName();
            future.complete(Bukkit.getWorld(name));
            if (saveInFile) {
                ConfigManager.getWorldConfig().getWorlds().put(name, worldData);
                ConfigManager.getWorldConfig().save();
            }
        };
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this.plugin, runnable);
        } else {
            runnable.run();
        }
        try {
            return future.get();
        } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}