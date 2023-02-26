package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.util.cache.CacheModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface APlayerCache extends CacheModel<UUID, APlayer> {
    @NotNull static APlayerCache instance() {
        return DestroyTheNexus.instance().playerCache();
    }

    @NotNull APlayer findOrCache(@NotNull Player player);

}
