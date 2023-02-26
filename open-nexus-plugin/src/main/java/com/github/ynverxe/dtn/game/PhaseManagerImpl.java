package com.github.ynverxe.dtn.game;

import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.util.time.MillisSnapshot;
import com.github.ynverxe.util.time.Temporizer;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import com.github.ynverxe.dtn.match.phase.PhaseHandler;
import com.github.ynverxe.dtn.match.phase.PhaseManager;

public class PhaseManagerImpl implements PhaseManager {
    private final PhaseHandler phaseHandler;
    private final int phaseQuantity;
    final List<Phase> phases;
    private int cursor;
    
    public PhaseManagerImpl(Rules rules, PhaseHandler phaseHandler, MatchImpl match) {
        this.phases = new ArrayList<>();
        this.cursor = -1;
        this.phaseHandler = phaseHandler;

        for (int i = 1; i <= phaseHandler.numbOfPhases(); ++i) {
            int time = rules.getPhaseDuration(i);
            Temporizer temporizer = new Temporizer(this::startNextPhase, time, TimeUnit.SECONDS);
            match.temporizers.add(temporizer);
            this.phases.add(new Phase(temporizer, i));
        }

        this.phaseQuantity = phaseHandler.numbOfPhases();
    }

    @Override
    public boolean isInitialized() {
        return this.cursor != -1;
    }

    @Override
    public boolean hasPhasesToStart() {
        return this.phases.size() - 1 > this.cursor;
    }

    @Override
    public boolean hasRunningPhase() {
        return this.isInitialized() && !this.currentPhase().temporizer.isOver();
    }

    @Override
    public void finalizeCurrentPhase() {
        if (!this.isInitialized()) {
            throw new IllegalStateException("Manager is not initialized");
        }

        if (!this.hasPhasesToStart()) {
            throw new IllegalStateException("No phases to start");
        }

        Phase phase = this.currentPhase();
        if (!phase.temporizer.isOver()) {
            phase.temporizer.setMillis(0L);
        }
    }

    @Override
    public int phaseQuantity() {
        return this.phaseQuantity;
    }

    @Override
    public int currentPhaseNumber() {
        if (!this.isInitialized()) {
            return -1;
        }

        return this.currentPhase().number;
    }
    
    @Override
    public @NotNull MillisSnapshot currentTemporizerMillis() {
        if (!this.isInitialized()) {
            return new MillisSnapshot(0L);
        }
        return this.currentPhase().temporizer.toMillisSnapshot();
    }
    
    void startNextPhase() {
        if (!this.hasPhasesToStart()) {
            return;
        }

        Phase phase = this.phases.get(++this.cursor);
        this.phaseHandler.handlePhaseStart(phase.number);
    }
    
    private Phase currentPhase() {
        return this.phases.get(this.cursor);
    }
    
    private static class Phase
    {
        private final Temporizer temporizer;
        private final int number;
        
        public Phase(Temporizer temporizer, int number) {
            this.temporizer = temporizer;
            this.number = number;
        }
    }
}
