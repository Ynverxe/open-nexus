package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.game.expansion.GameExpansion;
import com.github.ynverxe.dtn.model.instance.Named;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public interface GameRoom extends Named {
    @NotNull Date creationDate();

    @NotNull String typeName();

    void applyType(@NotNull String typeName);

    boolean enabled();

    void changeEnablement(boolean b);

    @Nullable GameInstance instance();

    void discardInstance() throws IllegalStateException;

    @NotNull GameLobby lobby();

    default @NotNull Location lobbySpawn() {
        return lobby().world().getSpawnLocation();
    }

    @NotNull GameLobby rebuildLobby(@NotNull Dimension dimension);

    @NotNull GameExpansion expansion();

    @NotNull Rules rules();

    void applyRules(@NotNull Rules rules);

    void terminate();

    static @NotNull GameManager manager() {
        return GameManager.instance();
    }
}
