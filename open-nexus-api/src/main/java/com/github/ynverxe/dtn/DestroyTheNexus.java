package com.github.ynverxe.dtn;

import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.logging.Logger;

public interface DestroyTheNexus extends ManagerPack {

    Logger LOGGER = ((Supplier<Logger>) (() -> {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");
        return plugin.getLogger();
    })).get();

    @NotNull static DestroyTheNexus instance() {
        final DestroyTheNexus destroyTheNexus = DTNSingletonContainer.dtnInstance();
        if (destroyTheNexus == null) {
            throw new IllegalStateException("not initialized yet");
        }
        return destroyTheNexus;
    }

    @NotNull World lobbyWorld();

    void setLobbyWorld(@NotNull World world);

    void sendToLobby(@NotNull APlayer p0);

    default boolean initialized() {
        return DTNSingletonContainer.dtnInstance() != null;
    }

}
