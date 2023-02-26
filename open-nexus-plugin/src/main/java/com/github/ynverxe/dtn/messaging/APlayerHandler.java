package com.github.ynverxe.dtn.messaging;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.translation.EntityHandler;

public class APlayerHandler implements EntityHandler<APlayer> {
    @Nullable
    public Object transformEntity(@NotNull APlayer entity) {
        return entity.bukkitPlayer();
    }
    
    @NotNull
    public Class<APlayer> representingType() {
        return APlayer.class;
    }
}
