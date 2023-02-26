package com.github.ynverxe.dtn.team;

import com.github.ynverxe.translation.resource.ResourceReference;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TeamColor {
    RED(Color.RED),
    YELLOW(Color.YELLOW),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN);

    private static final Map<String, TeamColor> VALUES;

    static {
        VALUES = Arrays.stream(values()).map(color -> new AbstractMap.SimpleEntry<>(color.lowerCaseName(), color))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private final Color color;
    private final ChatColor chatColor;

    TeamColor(Color color) {
        this.color = color;
        chatColor = ChatColor.valueOf(name());
    }

    public static @Nullable TeamColor byName(String name) {
        if (name == null) {
            return null;
        }

        return TeamColor.VALUES.get(name.toLowerCase());
    }

    public @NotNull Color bukkitColor() {
        return color;
    }

    public @NotNull String lowerCaseName() {
        return name().toLowerCase();
    }

    public @NotNull ChatColor chatColor() {
        return chatColor;
    }

    public @NotNull String formalName() {
        String name = lowerCaseName();
        StringBuilder builder = new StringBuilder(name);
        char first = name.charAt(0);
        builder.setCharAt(0, Character.toUpperCase(first));

        return builder.toString();
    }

    public String coloredName() {
        return chatColor + formalName();
    }

    public @NotNull ResourceReference<String> nameReference() {
        return ResourceReference.create(String.class, lowerCaseName() + "-name")
                .withDefaultProvider(this::coloredName);
    }

    public @NotNull ResourceReference<String> nexusNameReference() {
        return ResourceReference.create(String.class, lowerCaseName() + "-nexus-name").withDefaultValue(coloredName() + " nexus");
    }
}
