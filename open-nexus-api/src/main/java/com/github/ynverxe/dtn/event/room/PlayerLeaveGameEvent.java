package com.github.ynverxe.dtn.event.room;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveGameEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final GameInstance game;

    public PlayerLeaveGameEvent(APlayer player, GameInstance game) {
        super(player);
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return PlayerLeaveGameEvent.HANDLER_LIST;
    }

    public @NotNull GameInstance game() {
        return game;
    }

    public HandlerList getHandlers() {
        return PlayerLeaveGameEvent.HANDLER_LIST;
    }
}
