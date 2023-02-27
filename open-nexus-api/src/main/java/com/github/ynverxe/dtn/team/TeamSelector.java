package com.github.ynverxe.dtn.team;

import com.github.ynverxe.dtn.player.APlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface TeamSelector {

    @Nullable TeamColor getPlayerSelection(APlayer player);

    @NotNull Map<APlayer, TeamColor> collectPlayerSelections();

    @NotNull Map<TeamColor, List<APlayer>> collectAllSelections();

    @NotNull List<APlayer> collectTeamMembers(TeamColor color);

    void bindTeamToPlayer(APlayer player, TeamColor color);

    @Nullable TeamColor discardPlayerSelection(APlayer player);

}