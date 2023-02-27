package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.team.TeamSelector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameTeamSelectorImpl implements TeamSelector {

    private final Map<TeamColor, List<APlayer>> teamsSelections = new HashMap<>();
    private final GameInstanceImpl gameInstance;

    GameTeamSelectorImpl(GameInstanceImpl gameInstance) {
        this.gameInstance = gameInstance;

        for (TeamColor value : TeamColor.values()) {
            teamsSelections.put(value, new ArrayList<>());
        }
    }

    @Override
    public @Nullable TeamColor getPlayerSelection(APlayer player) {
        for (Map.Entry<TeamColor, List<APlayer>> entry : teamsSelections.entrySet()) {
            List<APlayer> players = entry.getValue();

            if (players.contains(player)) return entry.getKey();
        }

        return null;
    }

    @Override
    public @NotNull Map<APlayer, TeamColor> collectPlayerSelections() {
        Map<APlayer, TeamColor> collected = new HashMap<>();

        teamsSelections.forEach((k, v) -> v.forEach(player -> collected.put(player, k)));

        return collected;
    }

    @Override
    public @NotNull Map<TeamColor, List<APlayer>> collectAllSelections() {
        return Collections.unmodifiableMap(teamsSelections);
    }

    @Override
    public @NotNull List<APlayer> collectTeamMembers(TeamColor color) {
        return Collections.unmodifiableList(teamsSelections.get(color));
    }

    @Override
    public void bindTeamToPlayer(APlayer player, TeamColor color) {
        gameInstance.checkNotExternal(player);

        List<APlayer> players = teamsSelections.get(color);

        players.add(player);
        gameInstance.handleTeamSelection(player, color);
    }

    @Override
    public @Nullable TeamColor discardPlayerSelection(APlayer player) {
        gameInstance.checkNotExternal(player);

        for (Map.Entry<TeamColor, List<APlayer>> entry : teamsSelections.entrySet()) {
            List<APlayer> players = entry.getValue();

            if (players.remove(player)) {
                TeamColor teamColor = entry.getKey();

                gameInstance.handleSelectionDiscard(player, teamColor);
                return teamColor;
            }
        }

        return null;
    }
}