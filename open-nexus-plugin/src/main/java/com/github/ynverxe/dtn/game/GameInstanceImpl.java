package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.model.instance.AbstractTerminable;
import com.github.ynverxe.dtn.team.TeamSelector;
import com.github.ynverxe.util.RandomElementPicker;
import com.github.ynverxe.dtn.event.match.MatchEndEvent;
import com.github.ynverxe.dtn.game.expansion.FeatureHandler;
import com.github.ynverxe.dtn.game.expansion.GameExpansion;
import com.github.ynverxe.translation.resource.ResourceReference;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.authorization.Authorization;
import com.github.ynverxe.util.Pair;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.github.ynverxe.dtn.event.room.PlayerLeaveGameEvent;
import com.github.ynverxe.dtn.player.APlayerImpl;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.room.PlayerJoinGameEvent;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.util.time.MillisSnapshot;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.match.Match;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.DestroyTheNexus;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.ynverxe.util.time.Temporizer;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.util.vote.VotingContext;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.translation.Messenger;

public class GameInstanceImpl extends AbstractTerminable implements GameInstance {

    private final Messenger messenger;
    private final String name;
    private final UUID id;
    private final GameRoomImpl room;
    private final Rules rules;
    private final VotingContext<APlayer, String, Dimension> mapVotes;
    private final TeamSelector teamSelector;
    private final Temporizer temporizerToStart;
    private final Temporizer temporizerToEnd;
    private final List<APlayer> players;
    private final int minPlayersPerTeam;
    private final int maxPlayersPerTeam;
    private MatchImpl match;
    
    GameInstanceImpl(GameRoomImpl room) {
        this.id = UUID.randomUUID();
        this.players = new CopyOnWriteArrayList<>();
        this.room = room;
        this.rules = room.rules();
        this.name = room.name();
        final Rules rules = room.rules();
        this.minPlayersPerTeam = rules.minPlayersPerTeam();
        this.maxPlayersPerTeam = rules.maxPlayersPerTeam();
        this.temporizerToStart = new Temporizer(this::startMatch, room.rules().secondsToStart(), TimeUnit.SECONDS);
        this.temporizerToEnd = new Temporizer(this::terminate, room.rules().secondsToEndMatch(), TimeUnit.SECONDS);
        this.mapVotes = VotingContext.createContext(room.expansion()::findMapCandidates);
        this.teamSelector = new GameTeamSelectorImpl(this);
        this.messenger = DestroyTheNexus.instance().messenger();
    }
    
    @NotNull
    public String name() {
        return this.name;
    }
    
    @NotNull
    public UUID id() {
        return this.id;
    }
    
    @NotNull
    public GameRoom room() {
        this.checkNotTerminated();
        return this.room;
    }
    
    @NotNull
    public GameState state() {
        this.checkNotTerminated();
        if (this.match != null) {
            if (this.match.hasEnd()) {
                return GameState.ENDING;
            }

            return GameState.IN_GAME;
        } else {
            if (this.temporizerToStart.isStarted()) {
                return GameState.STARTING;
            }
            return GameState.WAITING;
        }
    }
    
    @Nullable
    public Match runningMatch() {
        this.checkNotTerminated();
        return this.match;
    }
    
    @NotNull
    public VotingContext<APlayer, String, Dimension> mapVotes() {
        this.checkNotTerminated();
        return this.mapVotes;
    }

    @Override
    public @NotNull TeamSelector teamSelector() {
        this.checkNotTerminated();
        return teamSelector;
    }

    private Temporizer runningInstanceTemporizer() {
        this.checkNotTerminated();
        switch (this.state()) {
            case ENDING: {
                return this.temporizerToEnd;
            }
            case WAITING:
            case STARTING: {
                return this.temporizerToStart;
            }
            default: {
                return null;
            }
        }
    }
    
    @NotNull
    public MillisSnapshot temporizerSnapshot() {
        Temporizer temporizer = this.runningInstanceTemporizer();
        if (temporizer == null) {
            return this.match.temporizerSnapshot();
        }
        return temporizer.toMillisSnapshot();
    }
    
    public int minPlayersPerTeam() {
        this.checkNotTerminated();
        return this.minPlayersPerTeam;
    }
    
    public int maxPlayersPerTeam() {
        this.checkNotTerminated();
        return this.maxPlayersPerTeam;
    }
    
    public void joinPlayer(@NotNull APlayer aPlayer) {
        if (this.isPlayerInGame(aPlayer)) {
            throw new IllegalArgumentException("Player '" + aPlayer.name() + "' is already in this room");
        }

        PlayerJoinGameEvent event = new PlayerJoinGameEvent(aPlayer, this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        this.players.add(aPlayer);
        ((APlayerImpl)aPlayer).handleGameJoin(this);

        MatchPlayer matchPlayer = MatchPlayer.resolveAPlayer(aPlayer);

        if (matchPlayer == null || !matchPlayer.isTeamAlive()) {
            aPlayer.teleport(this.room.lobby().world());
        }
    }
    
    public void leavePlayer(@NotNull APlayer aPlayer) {
        if (!this.isPlayerInGame(aPlayer)) {
            throw new IllegalStateException(aPlayer.name() + " is not in this room");
        }

        PlayerLeaveGameEvent event = new PlayerLeaveGameEvent(aPlayer, this);
        Bukkit.getPluginManager().callEvent(event);
        teamSelector.discardPlayerSelection(aPlayer);

        this.players.remove(aPlayer);
        ((APlayerImpl)aPlayer).handleRoomLeave();
        DestroyTheNexus.instance().sendToLobby(aPlayer);
    }
    
    public boolean isPlayerInGame(@NotNull APlayer aPlayer) {
        return this.players.contains(aPlayer);
    }
    
    @NotNull
    public List<APlayer> players() {
        return Collections.unmodifiableList(this.players);
    }
    
    protected void preTermination() {
        try {
            this.players.forEach(this::leavePlayer);

            if (this.match != null) {
                this.match.terminate();
            }

            this.match = null;
            this.mapVotes.clear();
            this.players.clear();
        } catch (Exception e) {
            Logger logger = DestroyTheNexus.LOGGER;
            logger.log(Level.SEVERE, "Unexpected exception has thrown while terminating game: " + this.name(), e);
        }

        this.room.handleInstanceTermination();
    }
    
    public @NotNull Pair<Authorization, Match> startMatch() {
        this.checkNotTerminated();

        Dimension dimension = this.mapVotes.candidateWithMostVotes();
        if (dimension == null) {
            return new Pair<>(new Authorization(DefaultTranslationContainer.NO_MAP_FOUND, false), null);
        }

        return this.startMatch(dimension);
    }

    public @NotNull Pair<Authorization, Match> startMatch(@NotNull Dimension dimension) {
        this.checkNotTerminated();
        if (!"match-map".equals(dimension.typeName())) {
            throw new IllegalArgumentException("Dimension is not of type 'match-type'");
        }

        GameExpansion expansion = this.room().expansion();
        FeatureHandler featureHandler = expansion.featureHandler();
        Authorization authorization = featureHandler.authorizeToStart(this, dimension);
        ResourceReference<?> message;

        if (!authorization.authorized()) {
            message = authorization.reasonMessage();
            restoreWaitingState();
            this.messenger.dispatchResource(this, message);
            return new Pair<>(authorization, null);
        }

        try {
            message = DefaultTranslationContainer.MATCH_START;

            createMatch(expansion, dimension);
        } catch (RuntimeException e) {
            restoreWaitingState();
            throw e;
        }

        if (this.rules.disableTeamSelection() && this.rules.enableRandomTeamJoin()) {
            this.players.forEach(player -> {
                if (teamSelector.getPlayerSelection(player) != null) return;

                TeamColor team = findRandomTeamForPlayer(player, players.size(), teamSelector.collectAllSelections());
                teamSelector.bindTeamToPlayer(player, team);
            });
        }

        this.messenger.dispatchResource(this, message);
        this.handleMatchStart();

        return new Pair<>(authorization, this.match);
    }
    
    @NotNull
    public Rules rules() {
        return this.room.rules();
    }
    
    void handleMatchEnd() {
        match.hasEnd = true;

        Bukkit.getPluginManager().callEvent(new MatchEndEvent(this.match));
        MillisSnapshot tempMillis = this.temporizerToEnd.toMillisSnapshot();

        this.messenger.dispatchResource(this, DefaultTranslationContainer.MATCH_WILL_END.replacing("<time>", tempMillis.formatMillis()));
    }
    
    private void handleMatchStart() {
        this.teamSelector.collectAllSelections().forEach((color, players) -> players.forEach(player -> this.match.joinPlayer(player, color)));
    }
    
    @NotNull
    public Iterator<APlayer> iterator() {
        return this.players().iterator();
    }
    
    void update() {
        GameState state = this.state();

        switch (state) {
            case WAITING:
            case STARTING: {
                this.updateWaitState();
                break;
            }
            case IN_GAME: {
                this.match.update();
                break;
            }
            case ENDING: {
                this.temporizerToEnd.elapseMillis(1L, TimeUnit.SECONDS);
                break;
            }
        }
    }
    
    private void updateWaitState() {
        boolean aptToStart = true;
        if (this.rules().enableRandomTeamJoin()) {
            aptToStart = (this.players.size() >= this.rules().minPlayersPerTeam() * 4);
        } else {
            for (List<APlayer> value : this.teamSelector.collectAllSelections().values()) {
                int onlineTeamPlayers = value.size();

                if (onlineTeamPlayers < this.minPlayersPerTeam) {
                    aptToStart = false;
                    break;
                }
            }
        }

        ResourceReference<?> message = null;
        if (aptToStart) {
            this.temporizerToStart.elapseMillis(1L, TimeUnit.SECONDS);
            MillisSnapshot millisSnapshot = this.temporizerToStart.toMillisSnapshot();

            if (millisSnapshot.convertMillis(TimeUnit.SECONDS) <= 10L) {
                message = DefaultTranslationContainer.COUNTDOWN_TO_START.replacing("<seconds>", millisSnapshot.formatMillis());
            }
        } else if (this.temporizerToStart.isStarted()) {
            this.temporizerToStart.restart();
            message = DefaultTranslationContainer.INSUFFICIENT_PLAYERS;
        }

        if (message != null) {
            this.messenger.dispatchResource(this.players, message);
        }
    }

    private void createMatch(GameExpansion expansion, Dimension dimension) {
        dimension.checkIsOfType("match-map");
        this.match = new MatchImpl(this, expansion.phaseHandlerFactory(), dimension);
    }

    private void restoreWaitingState() {
        if (this.temporizerToStart.isStarted()) {
            this.temporizerToStart.restart();
        }
    }

    void handleTeamSelection(APlayer player, TeamColor selected) {
        if (match == null) return;

        MatchPlayer matchPlayer = player.toMatchRepresent().orElse(null);

        if (matchPlayer == null) {
            match.joinPlayer(player, selected);
        } else {
            matchPlayer.joinTeam(selected);
        }
    }

    void handleSelectionDiscard(APlayer player, TeamColor discard) {
        if (match == null) return;

        player.toMatchRepresent().ifPresent(MatchPlayer::leaveTeam);
    }

    void checkNotExternal(APlayer player) {
        if (!players.contains(player)) throw new IllegalArgumentException("player doesn't belong to this game");
    }

    private static TeamColor findRandomTeamForPlayer(APlayer player, int totalPlayers, Map<TeamColor, ? extends List<?>> teams) {
        List<TeamColor> teamsToJoin = unbalancedTeams(totalPlayers, teams);
        if (teamsToJoin.isEmpty()) {
            teamsToJoin.addAll(Arrays.asList(TeamColor.values()));
        }

        return RandomElementPicker.pickRandom(teamsToJoin);
    }
    
    private static List<TeamColor> unbalancedTeams(int gamePlayers, Map<TeamColor, ? extends List<?>> teams) {
        List<TeamColor> unbalancedTeams = new ArrayList<>();
        int balance = gamePlayers / 4;

        teams.forEach((team, players) -> {
            int teamPlayerCount = players.size();

            if (teamPlayerCount < balance) {
                unbalancedTeams.add(team);
            }
        });

        return unbalancedTeams;
    }
}
