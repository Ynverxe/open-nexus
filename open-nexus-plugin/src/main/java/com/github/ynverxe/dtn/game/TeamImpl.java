package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.event.match.TeamDisqualifyEvent;
import com.github.ynverxe.dtn.match.Disqualifiable;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.match.NexusDestroyEvent;
import com.github.ynverxe.dtn.player.MatchPlayerImpl;
import java.util.Objects;
import com.github.ynverxe.util.RandomElementPicker;
import org.bukkit.Location;
import java.util.stream.Collectors;
import java.util.Collections;
import com.github.ynverxe.dtn.map.Nexus;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import com.github.ynverxe.dtn.player.MatchPlayer;
import java.util.List;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;

public class TeamImpl implements Team {
    private final Match match;
    private final TeamColor color;
    private final List<MatchPlayer> members;
    private boolean disqualified;
    
    public TeamImpl(Match match, TeamColor color) {
        this.members = new ArrayList<>();
        this.match = match;
        this.color = color;
    }
    
    @NotNull
    public Match match() {
        return this.match;
    }
    
    @NotNull
    public TeamColor color() {
        return this.color;
    }
    
    @NotNull
    public Nexus nexus() {
        return this.match.runningMap().teamNexus(this.color);
    }
    
    @NotNull
    public List<MatchPlayer> members() {
        return Collections.unmodifiableList(this.members);
    }
    
    @NotNull
    public List<MatchPlayer> nonDisqualifiedMembers() {
        return this.members.stream()
                .filter(player -> !player.disqualified())
                .collect(Collectors.toList());
    }
    
    @NotNull
    public List<Location> spawns() {
        return this.match.runningMap().teamSpawns(this.color);
    }
    
    @NotNull
    public Location randomSpawn() {
        return Objects.requireNonNull(RandomElementPicker.pickRandom(this.spawns()));
    }
    
    public boolean isNexusAlive() {
        return this.nexus().isAlive();
    }
    
    public void joinPlayer(@NotNull MatchPlayer matchPlayer) {
        if (this.members.contains(matchPlayer)) {
            throw new IllegalArgumentException("player is already in this team");
        }

        this.members.add(matchPlayer);
        ((MatchPlayerImpl)matchPlayer).handleTeamJoin(this.color);
    }
    
    public void leavePlayer(@NotNull MatchPlayer matchPlayer) {
        if (!this.members.contains(matchPlayer)) {
            throw new IllegalStateException("player is not in this team");
        }
        this.members.remove(matchPlayer);
        ((MatchPlayerImpl)matchPlayer).handleTeamLeave();
    }
    
    public boolean isMember(@NotNull MatchPlayer matchPlayer) {
        return this.members.contains(matchPlayer);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        TeamImpl team = (TeamImpl)o;
        return this.match.equals(team.match) && this.color == team.color;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.match, this.color);
    }
    
    public void disqualify() {
        this.disqualified = true;

        nonDisqualifiedMembers().forEach(Disqualifiable::disqualify);

        this.nexus().selfDestruct(NexusDestroyEvent.Reason.DISQUALIFY);

        Bukkit.getPluginManager().callEvent(new TeamDisqualifyEvent(this));
    }
    
    public boolean disqualified() {
        return this.disqualified;
    }

    void silentDisqualify() {
        this.disqualified = true;

        nonDisqualifiedMembers().forEach(Disqualifiable::disqualify);
    }
}
