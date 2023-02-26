package com.github.ynverxe.dtn.game.expansion;

import com.github.ynverxe.dtn.authorization.Authorization;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.PhaseHandlerFactory;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.game.type.GameType;
import com.github.ynverxe.dtn.match.TieBreaker;
import com.github.ynverxe.dtn.dimension.properties.MatchMapPropertiesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameExpansionImpl implements GameExpansion {

    private final GameType gameType;
    private final PhaseHandlerFactory phaseHandlerFactory;

    private TieBreaker tieBreaker = match -> null;
    private Supplier<Map<String, Dimension>> mapCandidatesSupplier = (() -> Dimension.manager().entries()
            .stream()
            .filter(entry -> entry.getValue().properties() instanceof MatchMapPropertiesContainer)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    private FeatureHandler featureHandler = new FeatureHandler() {
        @Override
        public @NotNull Authorization authorizeToStart(@NotNull GameInstance gameInstance, @NotNull Dimension selectedDimension) {
            return FeatureHandler.super.authorizeToStart(gameInstance, selectedDimension);
        }
    };

    public GameExpansionImpl(
            GameType gameType,
            PhaseHandlerFactory phaseHandlerFactory,
            TieBreaker tieBreaker,
            Supplier<Map<String, Dimension>> mapCandidatesSupplier,
            FeatureHandler featureHandler
    ) {
        this.gameType = gameType;
        this.phaseHandlerFactory = phaseHandlerFactory;
        if (tieBreaker != null) {
            this.tieBreaker = tieBreaker;
        }
        if (mapCandidatesSupplier != null) {
            this.mapCandidatesSupplier = mapCandidatesSupplier;
        }
        if (featureHandler != null) {
            this.featureHandler = featureHandler;
        }
    }

    @NotNull @Override
    public PhaseHandlerFactory phaseHandlerFactory() {
        return phaseHandlerFactory;
    }

    @NotNull @Override
    public Map<String, Dimension> findMapCandidates() {
        return mapCandidatesSupplier.get();
    }

    @NotNull @Override
    public GameType gameType() {
        return gameType;
    }

    @NotNull @Override
    public FeatureHandler featureHandler() {
        return featureHandler;
    }

    @NotNull @Override
    public TieBreaker tieBreaker() {
        return tieBreaker;
    }
}