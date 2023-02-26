package com.github.ynverxe.dtn.game;

import java.time.Duration;

import com.github.ynverxe.dtn.builder.MatchMapBuildContext;
import com.github.ynverxe.dtn.dataparsing.DefaultDimensionParsers;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.match.TieBreaker;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.github.ynverxe.dtn.operation.dimension.DimensionParseOperation;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.kit.Equipment;
import org.bukkit.entity.Player;
import com.github.ynverxe.dtn.kit.Kit;
import com.github.ynverxe.dtn.event.match.PreMatchTerminationEvent;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.player.MatchPlayerImpl;
import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.dtn.player.APlayer;
import java.util.Collections;
import com.github.ynverxe.dtn.match.phase.PhaseManager;
import com.github.ynverxe.util.time.MillisSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import com.github.ynverxe.dtn.metadata.SimpleDTNMetadataStore;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.github.ynverxe.dtn.DestroyTheNexus;

import java.util.List;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.PhaseHandlerFactory;
import com.github.ynverxe.dtn.map.MatchMap;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.player.MatchPlayer;
import java.util.UUID;
import java.util.Map;
import com.github.ynverxe.dtn.metadata.DTNMetadataStore;
import com.github.ynverxe.util.time.Temporizer;
import java.util.Queue;
import com.github.ynverxe.translation.Messenger;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.model.instance.AbstractTerminable;

public class MatchImpl extends AbstractTerminable implements Match {

    private final String name;
    private final Messenger messenger;
    private boolean executedTieBreaker;
    Queue<Temporizer> temporizers;
    private DTNMetadataStore metadataStore;
    private GameInstanceImpl holder;
    private PhaseManagerImpl phaseManager;
    private Map<UUID, MatchPlayer> players;
    private Map<TeamColor, Team> teams;
    private MatchMap runningMap;
    private Team winner;
    boolean hasEnd;

    MatchImpl(GameInstanceImpl holder, PhaseHandlerFactory phaseHandlerFactory, Dimension dimension) {
        this.messenger = DestroyTheNexus.instance().messenger();
        this.temporizers = new ConcurrentLinkedQueue<>();
        this.metadataStore = new SimpleDTNMetadataStore();
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.name = holder.name();
        this.holder = holder;
        this.phaseManager = new PhaseManagerImpl(holder.room().rules(), phaseHandlerFactory.createPhaseHandler(holder, this), this);

        for (TeamColor value : TeamColor.values()) {
            this.teams.put(value, new TeamImpl(this, value));
        }

        MatchMapBuildContext context = new MatchMapBuildContext(dimension, this);

        DimensionParseOperation<MatchMapBuildContext> operation = new DimensionParseOperation<>(
                DefaultDimensionParsers.MATCH_MAP_DATA_CONSUMER,
                context,
                false
        );

        operation.execute();

        this.runningMap = context.build();

        this.metadataStore.specifyValueType("nexus-damage-value", Integer.class, 1);
        this.metadataStore.specifyValueType("invincible-nexus", Boolean.class, true);
    }
    
    @NotNull
    public String name() {
        return this.name;
    }
    
    @NotNull
    public GameInstance holder() {
        this.checkNotTerminated();
        return this.holder;
    }
    
    @NotNull
    public DTNMetadataStore metadata() {
        this.checkNotTerminated();
        return this.metadataStore;
    }
    
    @NotNull
    public MillisSnapshot temporizerSnapshot() {
        return this.temporizers.isEmpty() ? new MillisSnapshot(0L) : this.temporizers.peek().toMillisSnapshot();
    }
    
    @NotNull
    public PhaseManager phaseManager() {
        this.checkNotTerminated();
        return this.phaseManager;
    }
    
    @NotNull
    public Map<UUID, MatchPlayer> players() {
        this.checkNotTerminated();
        return Collections.unmodifiableMap(this.players);
    }
    
    @NotNull
    public Map<TeamColor, Team> teams() {
        this.checkNotTerminated();
        return Collections.unmodifiableMap(this.teams);
    }
    
    @NotNull
    public MatchMap runningMap() {
        this.checkNotTerminated();
        return this.runningMap;
    }
    
    @NotNull
    public MatchPlayer addPlayer(@NotNull APlayer aPlayer, @NotNull TeamColor teamColor) {
        this.checkNotTerminated();
        if (this.players.containsKey(aPlayer.uuid())) {
            return this.players.get(aPlayer.uuid());
        }

        ComplexBoard complexBoard = aPlayer.board();
        BoardModel boardModel = aPlayer.findTranslationResource(DefaultTranslationContainer.IN_GAME_BOARD, null);

        boardModel.applyToBoard(complexBoard);

        MatchPlayer matchPlayer = new MatchPlayerImpl(aPlayer, this);
        this.players.put(matchPlayer.uuid(), matchPlayer);
        Team team = this.teams.get(teamColor);
        team.joinPlayer(matchPlayer);

        return matchPlayer;
    }
    
    protected void preTermination() {
        this.checkNotTerminated();
        Bukkit.getPluginManager().callEvent(new PreMatchTerminationEvent(this));

        this.runningMap.terminate();
        this.temporizers = null;
        this.holder = null;
        this.players = null;
        this.teams = null;
        this.phaseManager = null;
        this.runningMap = null;
        this.metadataStore = null;
    }
    
    public void preparePlayer(MatchPlayer matchPlayer, boolean resetEquipment) {
        this.checkNotTerminated();
        Team team = matchPlayer.team();
        if (team == null) {
            throw new IllegalArgumentException("Wtf, why want prepare a player without team?");
        }

        Kit kit = matchPlayer.kit();
        if (!matchPlayer.isOnline()) {
            throw new IllegalStateException("MatchPlayer: " + matchPlayer.name() + " is offline.");
        }

        Player player = matchPlayer.bukkitPlayer();
        if (resetEquipment) {
            player.getInventory().clear();
            Equipment equipment = kit.createEquipmentForPlayer(matchPlayer);
            equipment.armorPieces().forEach((k, v) -> k.apply(player, v));
            equipment.inventoryItems().forEach(player.getInventory()::setItem);
        }

        kit.preparePlayer(matchPlayer);
        matchPlayer.teleport(team.randomSpawn());
    }

    @Override
    public void handleTeamWinner(Team team) {
        this.checkNotTerminated();

        if (this.hasEnd()) {
            throw new IllegalStateException("Match has end");
        }

        List<Team> teams = nonDisqualifiedTeams();
        teams.remove(team);

        for (Team another : teams) {
            ((TeamImpl) another).silentDisqualify();
        }

        if ((this.winner = team) == null) {
            this.handleTie();
        } else {
            this.holder.handleMatchEnd();
            this.messenger.dispatchResource(this.holder, DefaultTranslationContainer.TEAM_HAS_WON.replacing("<team>", team.color().nameReference()));
            this.messenger.dispatchResource(team.members(), DefaultTranslationContainer.YOUR_TEAM_HAS_WON);
        }
    }

    public @Nullable Team winner() {
        return this.winner;
    }
    
    @NotNull
    public List<Team> nonDisqualifiedTeams() {
        this.checkNotTerminated();
        return this.teams.values().stream().filter(team -> !team.disqualified()).collect(Collectors.toList());
    }
    
    public boolean hasEnd() {
        return this.hasEnd;
    }

    @Override
    public List<Nexus> aliveNexuses() {
        return nonDisqualifiedTeams()
                .stream()
                .map(Team::nexus)
                .filter(Nexus::isAlive)
                .collect(Collectors.toList());
    }

    @Override
    public void skipTemporizer() {
        Temporizer temporizer = temporizers.poll();

        if (temporizer == null) return;

        temporizer.setMillis(0);
    }

    @Override
    public boolean hasRemainingTemporizers() {
        return temporizers.size() != 0;
    }

    void update() {
        synchronized (this) {
            if (this.hasEnd()) {
                return;
            }

            if (!this.phaseManager.isInitialized()) {
                this.phaseManager.startNextPhase();
            }

            Temporizer temporizer = this.temporizers.element();
            temporizer.elapseMillis(1L, TimeUnit.SECONDS);
            if (temporizer.isOver()) {
                this.temporizers.remove(temporizer);
            }

            if (this.temporizers.isEmpty()) {
                this.handleAllTemporizersTerminated();
            }
        }
    }
    
    public String toString() {
        return "{room=" + this.holder.name() + ", match=" + this.name() + ", gameTypeName=" + this.room().expansion().gameType().name() + "}";
    }
    
    void handleAllTemporizersTerminated() {
        if (!this.executedTieBreaker) {
            TieBreaker tieBreaker = this.room().expansion().tieBreaker();
            this.executedTieBreaker = true;
            Duration tieBreakDuration = tieBreaker.runTieBreak(this);

            if (tieBreakDuration != null) {
                this.addTemporizer(new Temporizer(null, tieBreakDuration.toMillis()));
                return;
            }
        }

        if (!hasEnd) this.handleTie();
    }
    
    private void handleTie() {
        this.holder.handleMatchEnd();
        this.temporizers.clear();
        this.messenger.dispatchResource(this.players.values(), DefaultTranslationContainer.MATCH_TIE);
    }
    
    void addTemporizer(Temporizer temporizer) {
        this.temporizers.add(temporizer);
    }
}
