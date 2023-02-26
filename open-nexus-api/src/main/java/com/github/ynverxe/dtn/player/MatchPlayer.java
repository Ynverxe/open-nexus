package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.kit.Kit;
import com.github.ynverxe.dtn.match.Disqualifiable;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.util.time.TimestampedValue;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface MatchPlayer extends PlayerBase, Disqualifiable {

    @NotNull APlayer handle();

    @NotNull Match match();

    default @NotNull GameRoom matchRoom() {
        return match().room();
    }

    @Nullable Team team();

    @NotNull TimestampedValue<Entity> lastDamager();

    boolean isTeamAlive();

    void joinTeam(@NotNull TeamColor team);

    void leaveTeam();

    void applyKit(@Nullable Kit kit);

    @NotNull Kit kit();

    default boolean isTeamFriend(@NotNull MatchPlayer other) {
        return Optional.ofNullable(team()).map(team -> team.equals(other.team())).orElse(false);
    }

    default @NotNull String coloredName() {
        return (team() == null) ? handle().name() : (team().color().chatColor() + handle().name());
    }

    static @Nullable MatchPlayer resolveAPlayer(@NotNull APlayer player) {
        Match match = player.playingMatch();

        return (match != null) ? match.players().get(player.uuid()) : null;
    }

    static @Nullable MatchPlayer fromEntity(@NotNull Entity entity) {
        APlayer player = APlayer.resolveEntity(entity);

        return player != null ? resolveAPlayer(player) : null;
    }
}
