package com.github.ynverxe.dtn.event.player;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinToLobby extends PlayerEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    public PlayerJoinToLobby(@NotNull APlayer player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return PlayerJoinToLobby.HANDLER_LIST;
    }

    public HandlerList getHandlers() {
        return PlayerJoinToLobby.HANDLER_LIST;
    }
}
