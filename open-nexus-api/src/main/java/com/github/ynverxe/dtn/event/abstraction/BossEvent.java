package com.github.ynverxe.dtn.event.abstraction;

import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.match.Match;
import org.jetbrains.annotations.NotNull;

public abstract class BossEvent extends MatchEvent {
    private final @NotNull PreparedBoss boss;

    public BossEvent(@NotNull Match match, @NotNull PreparedBoss boss) {
        super(match);
        this.boss = boss;
    }

    public PreparedBoss boss() {
        return boss;
    }
}