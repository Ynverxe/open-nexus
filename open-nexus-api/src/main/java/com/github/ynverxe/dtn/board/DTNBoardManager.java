package com.github.ynverxe.dtn.board;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.util.updater.ConjunctEntityUpdater;
import com.github.ynverxe.util.cache.CacheModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface DTNBoardManager extends CacheModel<UUID, ComplexBoard>, ConjunctEntityUpdater {
    void registerBoard(@NotNull UUID uuid, @NotNull ComplexBoard board);

    @NotNull ComplexBoard createBoard(@NotNull UUID uuid, @Nullable final UpdateMechanism updateMechanism);

    @NotNull ComplexBoard createAnonymousBoard(@Nullable final UpdateMechanism updateMechanism);

    static @NotNull DTNBoardManager instance() {
        return DestroyTheNexus.instance().boardManager();
    }
}
