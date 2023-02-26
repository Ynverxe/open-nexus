package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.authorization.Authorization;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.exception.MatchInitializeException;
import com.github.ynverxe.dtn.model.instance.Named;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.util.Pair;
import com.github.ynverxe.util.vote.VotingContext;
import com.github.ynverxe.util.time.MillisSnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GameInstance extends Named, Iterable<APlayer>, Terminable {
    @NotNull UUID id();

    @NotNull GameRoom room();

    @NotNull GameState state();

    @Nullable Match runningMatch();

    @NotNull VotingContext<APlayer, String, Dimension> mapVotes();

    @NotNull Map<TeamColor, List<APlayer>> readyPlayers();

    @NotNull MillisSnapshot temporizerSnapshot();

    int minPlayersPerTeam();

    int maxPlayersPerTeam();

    void handleReadyPlayer(@NotNull APlayer player, @NotNull TeamColor team);

    @Nullable TeamColor removeReadyPlayer(@NotNull APlayer p0);

    void joinPlayer(@NotNull APlayer p0);

    void leavePlayer(@NotNull APlayer p0);

    boolean isPlayerInGame(@NotNull APlayer p0);

    @NotNull List<APlayer> players();

    @NotNull Pair<Authorization, Match> startMatch() throws MatchInitializeException;

    @NotNull Pair<Authorization, Match> startMatch(@NotNull Dimension p0) throws MatchInitializeException;

    @Nullable TeamColor findPlayerTeam(@NotNull APlayer p0);

    @NotNull Rules rules();
}
