package com.github.ynverxe.dtn.event.abstraction;

import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class PlayerEvent extends Event {
    private final APlayer player;

    protected PlayerEvent(APlayer player) {
        this.player = Objects.requireNonNull(player, "player");
    }

    public @NotNull APlayer player() {
        return player;
    }

    public @Nullable MatchPlayer matchPlayer() {
        return MatchPlayer.resolveAPlayer(player);
    }

    public @NotNull Player bukkitPlayer() {
        return player.bukkitPlayer();
    }
}
