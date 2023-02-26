package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.event.abstraction.MatchEvent;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.event.HandlerList;

public class PhaseStartEvent extends MatchEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final int phase;

    public PhaseStartEvent(Match match, int phase) {
        super(match);
        this.phase = phase;
    }

    public static HandlerList getHandlerList() {
        return PhaseStartEvent.HANDLER_LIST;
    }

    public int phase() {
        return phase;
    }

    public HandlerList getHandlers() {
        return PhaseStartEvent.HANDLER_LIST;
    }
}
