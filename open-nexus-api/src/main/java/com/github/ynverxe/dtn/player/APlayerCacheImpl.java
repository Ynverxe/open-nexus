package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.event.player.APlayerRegisterEvent;
import com.github.ynverxe.util.cache.CacheModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APlayerCacheImpl implements APlayerCache {
    private final Logger logger;
    private final CacheModel.Mutable<UUID, APlayer> cachedPlayers;

    public APlayerCacheImpl() {
        cachedPlayers = CacheModel.create();
        logger = DestroyTheNexus.LOGGER;
    }

    @NotNull @Override
    public APlayer findOrCache(@NotNull Player player) {
        return safeGet(player.getUniqueId()).orElseGet(() -> {
            APlayer aPlayer = new APlayerImpl(player);

            cachedPlayers.set(player.getUniqueId(), aPlayer);
            logger.log(Level.INFO, player.getName() + " registered as APlayer.");

            Bukkit.getPluginManager().callEvent( new APlayerRegisterEvent(aPlayer));
            return aPlayer;
        });
    }

    @Override
    public @Nullable APlayer get(@NotNull UUID key) {
        return cachedPlayers.get(key);
    }

    @NotNull @Override
    public Optional<APlayer> safeGet(@NotNull UUID key) {
        return cachedPlayers.safeGet(key);
    }

    @NotNull @Override
    public Set<UUID> keys() {
        return Collections.unmodifiableSet(cachedPlayers.keys());
    }

    @NotNull @Override
    public Collection<APlayer> values() {
        return Collections.unmodifiableCollection(cachedPlayers.values());
    }

    @NotNull @Override
    public Set<Map.Entry<UUID, APlayer>> entries() {
        return Collections.unmodifiableSet(cachedPlayers.entries());
    }

    @Override
    public boolean has(@NotNull UUID key) {
        return cachedPlayers.has(key);
    }

    @Override
    public int cachedSize() {
        return cachedPlayers.cachedSize();
    }
}
