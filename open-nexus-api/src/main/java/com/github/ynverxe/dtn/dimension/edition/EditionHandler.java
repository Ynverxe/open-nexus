package com.github.ynverxe.dtn.dimension.edition;

import com.github.ynverxe.dtn.player.APlayer;
import org.jetbrains.annotations.NotNull;

public interface EditionHandler {
    void prepareAgent(@NotNull APlayer p0);

    void save();
}
