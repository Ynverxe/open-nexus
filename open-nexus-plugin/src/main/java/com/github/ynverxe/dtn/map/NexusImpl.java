package com.github.ynverxe.dtn.map;

import com.github.ynverxe.dtn.event.match.NexusDestroyEvent;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.match.NexusDamageEvent;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;
import com.github.ynverxe.dtn.team.Team;

public class NexusImpl implements Nexus {

    private final Team team;
    private final Location worldLocation;
    private final int totalHealth;
    private int health;
    
    public NexusImpl(Team team, Location location, int totalHealth) {
        this.team = team;
        this.worldLocation = location;
        this.totalHealth = totalHealth;
        this.health = totalHealth;
    }
    
    @NotNull
    public Team team() {
        return this.team;
    }
    
    @NotNull
    public Location location() {
        return this.worldLocation;
    }
    
    public int totalHealth() {
        return this.totalHealth;
    }
    
    public int remainingHealth() {
        return this.health;
    }
    
    public void setHealth(int health) {
        if (health <= 0) {
            throw new IllegalArgumentException("health <= 0, use Nexus#selfDestruct to kill the nexus");
        }
        this.health = health;
    }
    
    public void hit(@Nullable final MatchPlayer matchPlayer, int damage) {
        if (this.health <= 0) {
            throw new UnsupportedOperationException("nexus has no health");
        }

        if (damage <= 0) {
            throw new IllegalArgumentException("damage cannot be less than 1");
        }

        if (matchPlayer != null && this.team.equals(matchPlayer.team())) {
            throw new IllegalArgumentException("player is hitting his own team nexus wtf");
        }

        NexusDamageEvent event = new NexusDamageEvent(this.team.match(), this, matchPlayer, (matchPlayer != null) ? NexusDamageEvent.Reason.PLAYER : NexusDamageEvent.Reason.MAGIC, damage);
        damage = event.damage();
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        if ((this.health -= damage) <= 0) {
            NexusDestroyEvent destroyEvent = new NexusDestroyEvent(this, matchPlayer, (matchPlayer != null) ? NexusDestroyEvent.Reason.PLAYER : NexusDestroyEvent.Reason.MAGIC);
            Bukkit.getPluginManager().callEvent(destroyEvent);
        }
    }
    
    public boolean isAlive() {
        return this.health > 0;
    }
    
    public boolean selfDestruct(@NotNull NexusDestroyEvent.Reason reason) {
        if (this.health <= 0) {
            return false;
        }

        NexusDestroyEvent event = new NexusDestroyEvent(this, null, reason);
        Bukkit.getPluginManager().callEvent(event);
        this.health = 0;
        return true;
    }
}
