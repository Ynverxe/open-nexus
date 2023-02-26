package com.github.ynverxe.dtn.game.expansion;

import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.PhaseHandlerFactory;
import com.github.ynverxe.dtn.game.type.GameType;
import com.github.ynverxe.dtn.match.TieBreaker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public interface GameExpansion {
    @NotNull Map<String, Dimension> findMapCandidates();

    @NotNull PhaseHandlerFactory phaseHandlerFactory();

    @NotNull GameType gameType();

    @NotNull FeatureHandler featureHandler();

    @NotNull TieBreaker tieBreaker();

    static @NotNull GameExpansion create(@NotNull GameType gameType, @NotNull PhaseHandlerFactory phaseHandlerFactory, @Nullable final TieBreaker tieBreaker, @Nullable final FeatureHandler featureHandler, @Nullable final Supplier<Map<String, Dimension>> mapCandidatesSupplier) {
        return new GameExpansionImpl(gameType, phaseHandlerFactory, tieBreaker, mapCandidatesSupplier, featureHandler);
    }

    static @NotNull GameExpansion create(@NotNull GameType gameType, @NotNull PhaseHandlerFactory phaseHandlerFactory) {
        return create(gameType, phaseHandlerFactory, null, null, null);
    }
}
