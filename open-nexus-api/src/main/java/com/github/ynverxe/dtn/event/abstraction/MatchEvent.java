package com.github.ynverxe.dtn.event.abstraction;

import com.github.ynverxe.dtn.match.Match;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MatchEvent extends Event {
    private final @NotNull Match match;

    protected MatchEvent(@NotNull Match match) {
        this.match = match;
    }

    public Match match() {
        return match;
    }
}
