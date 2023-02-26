package com.github.ynverxe.dtn.game;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface GameLobby {
    @NotNull String dimensionName();

    @NotNull World world();

    void rebuildUtilities();
}
