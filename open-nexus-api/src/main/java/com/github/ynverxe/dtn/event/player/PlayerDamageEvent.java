package com.github.ynverxe.dtn.event.player;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final EntityDamageEvent handleEvent;

    public PlayerDamageEvent(APlayer player, EntityDamageEvent handleEvent) {
        super(player);
        this.handleEvent = handleEvent;
    }

    public static HandlerList getHandlerList() {
        return PlayerDamageEvent.HANDLER_LIST;
    }

    public @NotNull EntityDamageEvent handleEvent() {
        return handleEvent;
    }

    public HandlerList getHandlers() {
        return PlayerDamageEvent.HANDLER_LIST;
    }

    public boolean isCancelled() {
        return handleEvent.isCancelled();
    }

    public void setCancelled(boolean b) {
        handleEvent.setCancelled(b);
    }
}
