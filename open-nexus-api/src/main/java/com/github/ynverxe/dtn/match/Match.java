package com.github.ynverxe.dtn.match;

import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.model.instance.Named;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.dtn.map.MatchMap;
import com.github.ynverxe.dtn.match.phase.PhaseManager;
import com.github.ynverxe.dtn.metadata.DTNMetadataStore;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.util.time.MillisSnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Match extends Named, Terminable {

    @NotNull GameInstance holder();

    @NotNull DTNMetadataStore metadata();

    default @NotNull GameRoom room() {
        return holder().room();
    }

    @NotNull MillisSnapshot temporizerSnapshot();

    @NotNull PhaseManager phaseManager();

    @NotNull Map<UUID, MatchPlayer> players();

    @NotNull Map<TeamColor, Team> teams();

    @NotNull MatchMap runningMap();

    @NotNull MatchPlayer joinPlayer(@NotNull APlayer p0, @NotNull TeamColor p1);

    void preparePlayer(MatchPlayer p0, boolean p1);

    void handleTeamWinner(Team p0);

    @Nullable Team winner();

    @NotNull List<Team> nonDisqualifiedTeams();

    boolean hasEnd();

    List<Nexus> aliveNexuses();

    void skipTemporizer();

    boolean hasRemainingTemporizers();

}
