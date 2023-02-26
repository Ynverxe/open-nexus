package com.github.ynverxe.dtn.board;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface DTNBoard {
    int MAX_LINES_PER_BOARD = 15;

    @NotNull UUID id();

    void setTitle(@NotNull String title);

    void setLine(int index, @NotNull String line);

    void setLines(@NotNull List<String> lines);

    @Nullable String removeLine(int index);

    @Nullable String getLineText(int index);

    @NotNull List<String> lines();

    void clearLines();

    void display(@NotNull Player player);

    @NotNull CompositeTextInterceptor compositeTextInterceptor();

    boolean isDisplayed();

    @NotNull Player player();
}
