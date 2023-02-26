package com.github.ynverxe.dtn.papi;

import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class TeamPlaceholderExpansion extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "team";
    }
    
    @NotNull
    public String getAuthor() {
        return "Ynverxe";
    }
    
    @NotNull
    public String getVersion() {
        return "1.0";
    }
    
    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        APlayer aPlayer = APlayer.resolvePlayer(player);
        Match match = aPlayer.playingMatch();
        if (match == null) {
            return null;
        }

        String[] separatedRequest = params.split("_");
        TeamColor teamColor = TeamColor.byName(separatedRequest[0]);
        if (teamColor == null) {
            return String.format("Team '%s' doesn't exists", separatedRequest[0]);
        }

        if (separatedRequest.length == 1) {
            return teamColor.coloredName();
        }

        Team team = match.teams().get(teamColor);
        String teamField = separatedRequest[1];
        if ("health".equalsIgnoreCase(teamField)) {
            return team.nexus().remainingHealth() + "";
        }

        return null;
    }
}
