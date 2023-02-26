package com.github.ynverxe.dtn.board;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import java.util.function.BiFunction;

public class PAPILineInterceptor implements BiFunction<Player, String, String> {
    @Override
    public String apply(Player player, String s) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }
}
