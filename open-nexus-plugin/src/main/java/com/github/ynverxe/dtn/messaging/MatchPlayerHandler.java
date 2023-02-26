package com.github.ynverxe.dtn.messaging;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.translation.EntityHandler;

public class MatchPlayerHandler implements EntityHandler<MatchPlayer> {
    @Nullable
    public Object transformEntity(@NotNull MatchPlayer entity) {
        return entity.handle();
    }
    
    @NotNull
    public Class<MatchPlayer> representingType() {
        return MatchPlayer.class;
    }
}
