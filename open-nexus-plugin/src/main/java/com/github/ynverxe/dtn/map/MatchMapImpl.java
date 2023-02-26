package com.github.ynverxe.dtn.map;

import com.github.ynverxe.dtn.model.instance.AbstractTerminable;
import com.github.ynverxe.dtn.world.WorldContainer;
import org.bukkit.Bukkit;
import java.util.Objects;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.tickable.TickableRegistry;
import com.github.ynverxe.dtn.runnable.NexusThrobbingEffectRender;
import com.github.ynverxe.dtn.metadata.SimpleDTNMetadataStore;
import com.github.ynverxe.dtn.runnable.CompositeRunnableImpl;
import com.github.ynverxe.dtn.runnable.CompositeRunnable;
import com.github.ynverxe.dtn.metadata.DTNMetadataStore;
import com.github.ynverxe.dtn.boss.PreparedBoss;
import org.bukkit.Location;
import com.github.ynverxe.dtn.team.TeamColor;
import java.util.Map;
import java.util.List;

import com.github.ynverxe.dtn.match.Match;

public class MatchMapImpl extends AbstractTerminable implements MatchMap {

    private final String name;
    private Match match;
    private World mainAWorld;
    private WorldContainer worldContainer;
    private Map<TeamColor, List<Location>> teamSpawns;
    private Map<TeamColor, Nexus> teamNexusMap;
    private List<PreparedBoss> bosses;
    private List<Location> specialDiamonds;
    private final int diamondRegenTime;
    private DTNMetadataStore metadata;
    private BlockRegenerator blockRegenerator;
    private CompositeRunnable runnables;
    
    public MatchMapImpl(
            Match match,
            WorldContainer worldContainer,
            Map<TeamColor, List<Location>> teamSpawns,
            Map<TeamColor, Nexus> teamNexusMap,
            List<PreparedBoss> bosses,
            List<Location> specialDiamonds,
            int diamondRegenTime
    ) {
        this.diamondRegenTime = diamondRegenTime;
        this.blockRegenerator = new BlockRegeneratorImpl(this);
        this.runnables = new CompositeRunnableImpl();
        this.name = "MatchMap[" + match.room().name() + "]";
        this.mainAWorld = worldContainer.getWorldByIndex(0);
        this.worldContainer = worldContainer;
        this.teamSpawns = teamSpawns;
        this.teamNexusMap = teamNexusMap;
        this.bosses = bosses;
        this.specialDiamonds = specialDiamonds;
        this.metadata = new SimpleDTNMetadataStore();
        this.runnables.addRunnable(new NexusThrobbingEffectRender(match), false);
        this.runnables.addRunnable(this.blockRegenerator, true);
        TickableRegistry.instance().registerNewTickableEntity(this);

        System.out.println(teamSpawns);
    }
    
    @NotNull
    public String name() {
        return this.name;
    }
    
    @NotNull
    public List<PreparedBoss> bosses() {
        this.checkNotTerminated();
        return this.bosses;
    }
    
    @NotNull
    public List<Location> specialDiamonds() {
        this.checkNotTerminated();
        return this.specialDiamonds;
    }
    
    @NotNull
    public Match match() {
        this.checkNotTerminated();
        return this.match;
    }
    
    @NotNull
    public World mainWorld() {
        this.checkNotTerminated();
        return this.mainAWorld;
    }
    
    @NotNull
    public WorldContainer worlds() {
        this.checkNotTerminated();
        return worldContainer;
    }
    
    @NotNull
    public List<Location> teamSpawns(@NotNull TeamColor teamColor) {
        this.checkNotTerminated();
        return this.teamSpawns.get(Objects.requireNonNull(teamColor, "teamColor"));
    }
    
    @NotNull
    public Nexus teamNexus(@NotNull TeamColor teamColor) {
        this.checkNotTerminated();
        return this.teamNexusMap.get(teamColor);
    }
    
    @NotNull
    public DTNMetadataStore metadata() {
        this.checkNotTerminated();
        return this.metadata;
    }
    
    @NotNull
    public BlockRegenerator blockRegenerator() {
        this.checkNotTerminated();
        return this.blockRegenerator;
    }
    
    @NotNull
    public CompositeRunnable runnables() {
        this.checkNotTerminated();
        return this.runnables;
    }

    @Override
    public int diamondsRegenTime() {
        return diamondRegenTime;
    }

    protected void preTermination() {
        TickableRegistry.instance().unregisterTickable(this);
        this.match = null;
        this.mainAWorld = null;
        this.worldContainer.worlds().removeIf(world -> {
            Bukkit.unloadWorld(world, false);
            return true;
        });
        this.worldContainer = null;
        this.teamSpawns.clear();
        this.teamSpawns = null;
        this.teamNexusMap.clear();
        this.teamNexusMap = null;
        this.bosses.clear();
        this.bosses = null;
        this.blockRegenerator = null;
        this.runnables.runnableList().clear();
        this.runnables = null;
        this.specialDiamonds.clear();
        this.specialDiamonds = null;
        this.metadata.clear();
        this.metadata = null;
    }
    
    void checkNotExternal(Location location) {
        World aWorld = location.getWorld();

        if (!this.worldContainer.worlds().contains(aWorld)) {
            throw new IllegalArgumentException("external world '" + location.getWorld().getName() + "'");
        }
    }
    
    public void tick() {
        this.checkNotTerminated();
        this.runnables.run();
    }

    void checkNotTerminated0() {
        this.checkNotTerminated();
    }
}