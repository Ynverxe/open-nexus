package com.github.ynverxe.dtn.event.match;

import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.event.abstraction.BossEvent;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BossPreparationEvent extends BossEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public BossPreparationEvent(@NotNull Match match, @NotNull PreparedBoss boss) {
        super(match, boss);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}