package com.github.ynverxe.util;

import com.github.ynverxe.dtn.exception.SingletonClassException;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class TextColorizer {
    private TextColorizer() {
        throw new SingletonClassException(getClass());
    }

    public @NotNull static String colorize(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
