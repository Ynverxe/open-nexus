package com.github.ynverxe.dtn.match.phase;

import com.github.ynverxe.util.time.MillisSnapshot;
import org.jetbrains.annotations.NotNull;

public interface PhaseManager {
    boolean isInitialized();

    boolean hasPhasesToStart();

    boolean hasRunningPhase();

    void finalizeCurrentPhase() throws IllegalStateException;

    int phaseQuantity();

    int currentPhaseNumber();

    @NotNull MillisSnapshot currentTemporizerMillis();
}
