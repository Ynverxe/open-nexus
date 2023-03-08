package com.github.ynverxe.dtn.player;

import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.dtn.event.match.MatchPlayerDisqualifyEvent;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.team.TeamColor;
import org.jetbrains.annotations.Nullable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import com.github.ynverxe.dtn.kit.KitRegistry;
import java.util.Objects;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import com.github.ynverxe.dtn.team.Team;
import org.bukkit.entity.Entity;
import com.github.ynverxe.util.time.TimestampedValue;
import com.github.ynverxe.dtn.kit.Kit;
import com.github.ynverxe.dtn.match.Match;

public class MatchPlayerImpl extends DelegatedPlayerBase implements MatchPlayer {

    private final APlayer aPlayer;
    private final Match match;
    private Kit kit;
    private final TimestampedValue<Entity> damager;
    private Team team;
    private boolean disqualified;
    
    public MatchPlayerImpl(APlayer aPlayer, Match match) {
        super(aPlayer);

        this.damager = new TimestampedValue<>(Duration.of(5L, ChronoUnit.SECONDS));
        this.aPlayer = Objects.requireNonNull(aPlayer, "anniPlayer");
        this.match = Objects.requireNonNull(match, "match");
        this.kit = KitRegistry.instance().defaultKit();
    }
    
    @NotNull
    public UUID uuid() {
        return this.aPlayer.uuid();
    }
    
    @NotNull
    public APlayer handle() {
        return this.aPlayer;
    }
    
    @NotNull
    public Player bukkitPlayer() {
        return this.aPlayer.bukkitPlayer();
    }
    
    @NotNull
    public Match match() {
        return this.match;
    }
    
    @Nullable
    public Team team() {
        return this.team;
    }
    
    @NotNull
    public TimestampedValue<Entity> lastDamager() {
        return this.damager;
    }
    
    public boolean isTeamAlive() {
        return team != null && !this.team.isNexusAlive();
    }
    
    public void joinTeam(@NotNull TeamColor teamColor) {
        if (this.team != null && this.team.color() == teamColor) {
            throw new IllegalArgumentException("player is already in that team");
        }

        Team newTeam = this.match.teams().get(teamColor);
        newTeam.joinPlayer(this);
    }
    
    public void leaveTeam() {
        if (this.team == null) {
            return;
        }
        this.team.leavePlayer(this);
    }
    
    public boolean disqualified() {
        return this.disqualified;
    }
    
    public void disqualify() {
        this.disqualified = true;
        Bukkit.getPluginManager().callEvent(new MatchPlayerDisqualifyEvent(this));
    }
    
    public void applyKit(@Nullable Kit kit) {
        if (kit == null) {
            kit = KitRegistry.instance().defaultKit();
        }
        this.kit = kit;
    }
    
    @NotNull
    public Kit kit() {
        return this.kit;
    }
    
    public final void handleTeamJoin(TeamColor teamColor) {
        Team newTeam = this.match.teams().get(teamColor);
        if (!newTeam.members().contains(this)) {
            throw new IllegalArgumentException("player is not on team: " + newTeam.color());
        }

        boolean noPreviousTeam = this.team == null;
        if (!noPreviousTeam) {
            this.team.leavePlayer(this);
        }

        this.team = newTeam;
        this.match.preparePlayer(this, noPreviousTeam);
    }
    
    public final void handleTeamLeave() {
        if (this.team.members().contains(this)) {
            throw new IllegalArgumentException("player is still in the team");
        }

        this.team = null;
    }
    
    public void renderResource(@NotNull Object resource, @Nullable FormattingContext context) {
        this.handle().renderResource(resource, context);
    }
}
