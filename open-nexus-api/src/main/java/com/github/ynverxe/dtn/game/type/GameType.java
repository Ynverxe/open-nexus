package com.github.ynverxe.dtn.game.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class GameType {

    private final String name;
    private final List<String> description;

    public GameType(@NotNull String name, @NotNull List<String> description) {
        this.name = name.toLowerCase();
        this.description = description;
    }

    public @NotNull String name() {
        return name;
    }

    public @NotNull List<String> description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameType gameType = (GameType) o;
        return name.equals(gameType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
