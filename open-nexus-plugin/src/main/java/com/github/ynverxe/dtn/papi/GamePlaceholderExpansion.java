package com.github.ynverxe.dtn.papi;

import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.game.Rules;
import com.github.ynverxe.dtn.game.GameInstance;
import java.util.Locale;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class GamePlaceholderExpansion extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "game";
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
        GameInstance game = aPlayer.playingGame();
        if (game == null) {
            return null;
        }

        params = params.toLowerCase(Locale.ROOT);
        Rules rules = game.rules();
        String s = params;

        switch (s) {
            case "min": {
                return rules.minPlayersPerTeam() + "";
            }
            case "max": {
                return rules.maxPlayersPerTeam() + "";
            }
            case "current": {
                return game.players().size() + "";
            }
            case "timer": {
                return game.temporizerSnapshot().formatMillis();
            }
            default: {
                return null;
            }
        }
    }
}
