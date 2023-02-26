package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.event.abstraction.MatchPlayerEvent;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.event.HandlerList;

public class MatchPlayerDisqualifyEvent extends MatchPlayerEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    public MatchPlayerDisqualifyEvent(MatchPlayer matchPlayer) {
        super(matchPlayer);
    }

    public static HandlerList getHandlerList() {
        return MatchPlayerDisqualifyEvent.HANDLER_LIST;
    }

    public HandlerList getHandlers() {
        return MatchPlayerDisqualifyEvent.HANDLER_LIST;
    }
}
