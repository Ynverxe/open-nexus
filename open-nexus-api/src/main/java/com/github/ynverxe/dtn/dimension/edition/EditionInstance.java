package com.github.ynverxe.dtn.dimension.edition;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.dtn.player.APlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EditionInstance extends Terminable {

    @NotNull Dimension dimension();

    @NotNull List<APlayer> agents();

    @NotNull EditionHandler configurationHandler();

    default @Nullable <T extends EditionHandler> T configurationHandler(@NotNull Class<T> clazz) {
        if (!clazz.isInstance(configurationHandler())) {
            return null;
        }

        return clazz.cast(configurationHandler());
    }

    void save(boolean p0, boolean p1);

    static @Nullable EditionInstance findByPlayer(@NotNull APlayer aPlayer) {
        return EditionInstanceManager.instance().findByAgent(aPlayer).orElse(null);
    }

    static @Nullable EditionInstance findByWorld(@NotNull String name) {
        return DestroyTheNexus.instance().configurationInstanceManager().get(name);
    }
}
