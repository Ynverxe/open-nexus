package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.event.abstraction.MatchEvent;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends MatchEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    protected MatchStartEvent(Match match) {
        super(match);
    }

    public static HandlerList getHandlerList() {
        return MatchStartEvent.HANDLER_LIST;
    }

    public HandlerList getHandlers() {
        return MatchStartEvent.HANDLER_LIST;
    }
}
