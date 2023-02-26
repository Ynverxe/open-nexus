package com.github.ynverxe.dtn.map;

import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.metadata.DTNMetadataStore;
import com.github.ynverxe.dtn.runnable.CompositeRunnable;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.tickable.Tickable;
import com.github.ynverxe.dtn.world.WorldContainer;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface MatchMap extends Tickable, Terminable {

    @NotNull List<PreparedBoss> bosses();

    @NotNull List<Location> specialDiamonds();

    @NotNull Match match();

    @NotNull World mainWorld();

    @NotNull WorldContainer worlds();

    @NotNull List<Location> teamSpawns(@NotNull TeamColor p0);

    @NotNull Nexus teamNexus(@NotNull TeamColor p0);

    @NotNull DTNMetadataStore metadata();

    @NotNull BlockRegenerator blockRegenerator();

    @NotNull CompositeRunnable runnables();

    int diamondsRegenTime();

}
