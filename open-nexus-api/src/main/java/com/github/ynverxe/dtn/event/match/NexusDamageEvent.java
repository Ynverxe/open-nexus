package com.github.ynverxe.dtn.event.match;

import com.avaje.ebean.validation.NotNull;
import com.github.ynverxe.dtn.event.abstraction.MatchEvent;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NexusDamageEvent extends MatchEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final Nexus nexus;
    private final MatchPlayer damager;
    private final Reason reason;
    private boolean cancelled;
    private int damage;

    public NexusDamageEvent(Match match, Nexus nexus, MatchPlayer damager, Reason reason, int damage) {
        super(match);
        this.nexus = nexus;
        this.damager = damager;
        this.reason = reason;
        this.damage = damage;
        if (reason == Reason.PLAYER && damager == null) {
            throw new IllegalArgumentException("player is null, but reason is PLAYER");
        }
    }

    public static HandlerList getHandlerList() {
        return NexusDamageEvent.HANDLER_LIST;
    }

    public @NotNull Nexus nexus() {
        return nexus;
    }

    public @NotNull MatchPlayer damager() {
        return damager;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean b) {
        cancelled = true;
    }

    public HandlerList getHandlers() {
        return NexusDamageEvent.HANDLER_LIST;
    }

    public int damage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("damage < 0");
        }
        this.damage = damage;
    }

    public @NotNull Reason reason() {
        return reason;
    }

    public enum Reason {
        PLAYER,
        MAGIC;
    }
}
