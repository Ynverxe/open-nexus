package com.github.ynverxe.dtn.game;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class GameLobbyImpl implements GameLobby {
    private final World aWorld;
    private final String dimensionName;
    
    GameLobbyImpl(World aWorld, String dimensionName) {
        this.aWorld = aWorld;
        this.dimensionName = dimensionName;
    }
    
    @NotNull
    public String dimensionName() {
        return this.dimensionName;
    }
    
    public @NotNull World world() {
        return this.aWorld;
    }
    
    public void rebuildUtilities() {
    }
}
