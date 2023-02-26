package com.github.ynverxe.dtn.papi;

import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class APlayerPlaceholderHolderExpansion extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "aplayer";
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
        if (!"team".equalsIgnoreCase(params)) {
            return null;
        }

        APlayer aPlayer = APlayer.resolvePlayer(player);
        GameInstance gameInstance = aPlayer.playingGame();
        if (gameInstance == null) {
            return null;
        }

        TeamColor teamColor = gameInstance.findPlayerTeam(aPlayer);
        if (teamColor == null) {
            return null;
        }

        return teamColor.formalName();
    }
}
