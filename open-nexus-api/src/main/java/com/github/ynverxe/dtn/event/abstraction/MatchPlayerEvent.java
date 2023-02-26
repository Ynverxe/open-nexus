package com.github.ynverxe.dtn.event.abstraction;

import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class MatchPlayerEvent extends MatchEvent {
    private final MatchPlayer matchPlayer;

    protected MatchPlayerEvent(MatchPlayer matchPlayer) {
        super(matchPlayer.match());
        this.matchPlayer = Objects.requireNonNull(matchPlayer);
    }

    public @NotNull Player bukkitPlayer() {
        return matchPlayer.bukkitPlayer();
    }

    public @NotNull MatchPlayer matchPlayer() {
        return matchPlayer;
    }
}
