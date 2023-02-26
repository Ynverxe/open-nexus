package com.github.ynverxe.dtn.game;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public final class GameRoomModel {
    private final String name;
    private final String typeName;
    private final String dimensionName;
    private final Date creationDate;
    private final Rules rules;
    private final boolean enabled;

    GameRoomModel(String name, Date creationDate, String typeName, String dimensionName, Rules rules, boolean enabled) {
        this.name = name;
        this.creationDate = creationDate;
        this.typeName = typeName;
        this.dimensionName = dimensionName;
        this.rules = rules;
        this.enabled = enabled;
    }

    public static @NotNull GameRoomModel fromRoom(@NotNull GameRoom room) {
        return create(room.name(), room.creationDate(), room.typeName(), room.lobby().dimensionName(), room.rules(), room.enabled());
    }

    public static @NotNull GameRoomModel create(String name, Date creationDate, String typeName, String lobbyWorldName, Rules rules, boolean enabled) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(creationDate, "creationDate");
        Objects.requireNonNull(typeName, "typeName");
        Objects.requireNonNull(rules, "rules");
        Objects.requireNonNull(lobbyWorldName, "lobbyWorldName");
        return new GameRoomModel(name, creationDate, typeName, lobbyWorldName, rules, enabled);
    }

    public @NotNull String name() {
        return name;
    }

    public @NotNull Date creationDate() {
        return creationDate;
    }

    public @NotNull String typeName() {
        return typeName;
    }

    public @NotNull String dimensionName() {
        return dimensionName;
    }

    public boolean enabled() {
        return enabled;
    }

    public @NotNull Rules rules() {
        return rules;
    }
}
