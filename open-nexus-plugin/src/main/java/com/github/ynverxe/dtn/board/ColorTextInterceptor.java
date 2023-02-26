package com.github.ynverxe.dtn.board;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.function.BiFunction;

public class ColorTextInterceptor implements BiFunction<Player, String, String> {
    @Override
    public String apply(Player player, String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
