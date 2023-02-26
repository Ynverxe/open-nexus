package com.github.ynverxe.dtn.team;

import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.match.Disqualifiable;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Team extends Disqualifiable {

    @NotNull Match match();

    @NotNull TeamColor color();

    @NotNull Nexus nexus();

    @NotNull List<MatchPlayer> members();

    @NotNull List<MatchPlayer> nonDisqualifiedMembers();

    @NotNull List<Location> spawns();

    @NotNull Location randomSpawn();

    boolean isNexusAlive();

    void joinPlayer(@NotNull MatchPlayer player);

    void leavePlayer(@NotNull MatchPlayer player);

    boolean isMember(@NotNull MatchPlayer player);

}
