package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.match.phase.PhaseHandler;
import org.jetbrains.annotations.NotNull;

public interface PhaseHandlerFactory {
    @NotNull PhaseHandler createPhaseHandler(@NotNull GameInstance p0, @NotNull Match p1);
}
