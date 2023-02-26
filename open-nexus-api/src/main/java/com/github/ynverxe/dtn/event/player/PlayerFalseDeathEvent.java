package com.github.ynverxe.dtn.event.player;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.HandlerList;

public class PlayerFalseDeathEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    public PlayerFalseDeathEvent(APlayer player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return PlayerFalseDeathEvent.HANDLER_LIST;
    }

    public HandlerList getHandlers() {
        return PlayerFalseDeathEvent.HANDLER_LIST;
    }
}
