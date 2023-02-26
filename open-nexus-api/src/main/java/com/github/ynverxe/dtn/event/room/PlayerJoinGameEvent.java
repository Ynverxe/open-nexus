package com.github.ynverxe.dtn.event.room;

import com.github.ynverxe.dtn.event.abstraction.PlayerEvent;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinGameEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final GameInstance game;
    private boolean cancelled;

    public PlayerJoinGameEvent(@NotNull APlayer player, @NotNull GameInstance game) {
        super(player);
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return PlayerJoinGameEvent.HANDLER_LIST;
    }

    public @NotNull GameInstance game() {
        return game;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public HandlerList getHandlers() {
        return PlayerJoinGameEvent.HANDLER_LIST;
    }
}
