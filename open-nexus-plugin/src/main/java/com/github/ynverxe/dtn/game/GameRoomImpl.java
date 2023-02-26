package com.github.ynverxe.dtn.game;

import java.util.Objects;

import com.github.ynverxe.dtn.model.instance.AbstractTerminable;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.world.WorldContainer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.game.expansion.GameExpansion;
import java.util.Date;

public class GameRoomImpl extends AbstractTerminable implements GameRoom {

    private static final String LOBBY_PREFIX_FORMAT = "GameRoom[%s]_";

    private final String name;
    private final Date creationDate;
    private final GameManagerImpl gameManager;
    private String typeName;
    private GameExpansion expansion;
    private Rules rules;
    private boolean enabled;
    private volatile GameInstance gameInstance;
    private volatile GameLobby lobby;
    
    GameRoomImpl(String name, Date creationDate, String typeName, GameExpansion expansion, Rules rules, boolean enabled, GameManagerImpl gameManager) {
        this.name = name;
        this.creationDate = creationDate;
        this.typeName = typeName;
        this.expansion = expansion;
        this.rules = rules;
        this.gameManager = gameManager;
        this.enabled = enabled;

        if (enabled) {
            this.gameInstance = new GameInstanceImpl(this);
        }
    }
    
    @NotNull
    public String name() {
        return this.name;
    }
    
    @NotNull
    public Date creationDate() {
        return this.creationDate;
    }
    
    @NotNull
    public String typeName() {
        return this.typeName;
    }
    
    public void applyType(@NotNull String typeName) {
        this.checkIfAptToEdit();

        GameExpansion gameExpansion = this.gameManager.findExpansion(typeName)
                .orElseThrow(() -> new IllegalArgumentException("No expansion found with type name:" + typeName));

        this.typeName = typeName;
        this.expansion = gameExpansion;
    }
    
    public boolean enabled() {
        return this.enabled;
    }
    
    public void changeEnablement(boolean b) {
        if (this.enabled && !b) {
            this.gameInstance.terminate();
            this.gameInstance = null;
        }

        if (!this.enabled && b) {
            this.gameInstance = new GameInstanceImpl(this);
        }

        this.enabled = b;
    }
    
    @Nullable
    public GameInstance instance() {
        return this.gameInstance;
    }
    
    public void discardInstance() throws IllegalStateException {
        if (!this.enabled) {
            throw new IllegalStateException("room is not enabled");
        }

        this.gameInstance.terminate();
    }
    
    @NotNull
    public GameLobby lobby() {
        return this.lobby;
    }
    
    @NotNull
    public GameLobby rebuildLobby(@NotNull Dimension dimension) {
        WorldContainer clonedWorlds = dimension.cloneWorlds(String.format("GameRoom[%s]_", this.name));
        return this.lobby = new GameLobbyImpl(clonedWorlds.getWorldByIndex(0), dimension.name());
    }
    
    @NotNull
    public GameExpansion expansion() {
        return this.expansion;
    }
    
    @NotNull
    public Rules rules() {
        return this.rules;
    }
    
    public void applyRules(@NotNull Rules rules) {
        this.checkIfAptToEdit();
        this.rules = Objects.requireNonNull(rules, "rules");
    }

    private void checkIfAptToEdit() {
        if (this.enabled) {
            throw new IllegalStateException("game is not disabled");
        }
    }
    
    void handleInstanceTermination() {
        this.gameInstance = new GameInstanceImpl(this);
    }

    @Override
    protected void preTermination() {
        if (gameInstance != null) {
            this.gameInstance.terminate();
            this.gameInstance = null;
        }

        lobby = null;

        gameManager.removeRoom(name);
    }
}
