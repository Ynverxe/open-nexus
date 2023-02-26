package com.github.ynverxe.dtn.event.player;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.HandlerList;

public class APlayerRegisterEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    public APlayerRegisterEvent(APlayer player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return APlayerRegisterEvent.HANDLER_LIST;
    }
}
