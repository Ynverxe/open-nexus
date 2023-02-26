package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.event.abstraction.MatchEvent;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NexusDestroyEvent extends MatchEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final Nexus nexus;
    private final MatchPlayer damager;
    private final Reason reason;

    public NexusDestroyEvent(Nexus nexus, MatchPlayer damager, Reason reason) {
        super(nexus.team().match());
        this.nexus = nexus;
        this.damager = damager;
        this.reason = reason;

        if (reason == Reason.PLAYER && damager == null) {
            throw new IllegalArgumentException("player is null, but reason is PLAYER");
        }
    }

    public static HandlerList getHandlerList() {
        return NexusDestroyEvent.HANDLER_LIST;
    }

    public @NotNull MatchPlayer damager() {
        return damager;
    }

    public @NotNull Reason reason() {
        return reason;
    }

    public @NotNull Nexus nexus() {
        return nexus;
    }

    public HandlerList getHandlers() {
        return NexusDestroyEvent.HANDLER_LIST;
    }

    public enum Reason {
        PLAYER,
        MAGIC,
        DISQUALIFY;
    }
}
