package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.event.abstraction.MatchEvent;
import com.github.ynverxe.dtn.team.Team;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamDisqualifyEvent extends MatchEvent {
    private static final HandlerList HANDLER_LIST;

    static {
        HANDLER_LIST = new HandlerList();
    }

    private final Team team;

    public TeamDisqualifyEvent(Team team) {
        super(team.match());
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return TeamDisqualifyEvent.HANDLER_LIST;
    }

    public @NotNull Team team() {
        return team;
    }

    public HandlerList getHandlers() {
        return TeamDisqualifyEvent.HANDLER_LIST;
    }
}
