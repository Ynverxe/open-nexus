package com.github.ynverxe.dtn.dimension.edition;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.EditionHandlerFactory;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.util.cache.CacheModel;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface EditionInstanceManager extends CacheModel<String, EditionInstance> {

    @NotNull EditionInstance configureDimension(@NotNull APlayer p0, @NotNull Dimension p1) throws IllegalArgumentException;

    @NotNull EditionInstance configureDimension(@NotNull APlayer p0, @NotNull String p1) throws IllegalArgumentException;

    @NotNull Optional<EditionInstance> findByAgent(@NotNull APlayer p0);

    @NotNull EditionInstanceManager addHandlerFactory(@NotNull String p0, @NotNull EditionHandlerFactory p1);

    static @NotNull EditionInstanceManager instance() {
        return DestroyTheNexus.instance().configurationInstanceManager();
    }
}
