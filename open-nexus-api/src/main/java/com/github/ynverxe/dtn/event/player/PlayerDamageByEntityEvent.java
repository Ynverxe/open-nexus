package com.github.ynverxe.dtn.event.player;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDamageByEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final Entity attacker;
    private final EntityDamageByEntityEvent handleEvent;

    public PlayerDamageByEntityEvent(APlayer player, Entity attacker, EntityDamageByEntityEvent handleEvent) {
        super(player);
        this.attacker = attacker;
        this.handleEvent = handleEvent;
    }

    public static HandlerList getHandlerList() {
        return PlayerDamageByEntityEvent.HANDLER_LIST;
    }

    public @NotNull Entity attacker() {
        return attacker;
    }

    public @Nullable MatchPlayer attackerPlayer() {
        return MatchPlayer.fromEntity(attacker);
    }

    public @NotNull EntityDamageByEntityEvent handleEvent() {
        return handleEvent;
    }

    public HandlerList getHandlers() {
        return PlayerDamageByEntityEvent.HANDLER_LIST;
    }

    public boolean isCancelled() {
        return handleEvent.isCancelled();
    }

    public void setCancelled(boolean b) {
        handleEvent.setCancelled(b);
    }
}
