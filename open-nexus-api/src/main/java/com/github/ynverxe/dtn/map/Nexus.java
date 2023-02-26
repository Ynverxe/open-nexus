package com.github.ynverxe.dtn.map;

import com.github.ynverxe.dtn.event.match.NexusDestroyEvent;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.team.Team;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Nexus {
    @NotNull Team team();

    @NotNull Location location();

    int totalHealth();

    int remainingHealth();

    void setHealth(int p0);

    void hit(@Nullable MatchPlayer p0, int p1);

    boolean isAlive();

    boolean selfDestruct(@NotNull NexusDestroyEvent.Reason p0);
}
