package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.model.instance.Copyable;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Rules implements Copyable {
    public static final Duration DEFAULT_PHASE_DURATION;

    static {
        DEFAULT_PHASE_DURATION = Duration.ofMinutes(10L);
    }

    private final Map<Integer, Integer> phaseDurations;
    private int minPlayersPerTeam;
    private int maxPlayersPerTeam;
    private int secondsToStart;
    private int secondsToEndMatch;
    private boolean disableTeamSelection;
    private boolean randomTeamJoin;

    public Rules(int minPlayersPerTeam, int maxPlayersPerTeam, int secondsToStart, int secondsToEndMatch, boolean randomTeamJoin, boolean disableTeamSelection, Map<Integer, Integer> phaseDurations) {
        this.minPlayersPerTeam = minPlayersPerTeam;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.secondsToStart = secondsToStart;
        this.secondsToEndMatch = secondsToEndMatch;
        this.randomTeamJoin = randomTeamJoin;
        this.disableTeamSelection = disableTeamSelection;
        this.phaseDurations = phaseDurations;
    }

    public Rules() {
        this(4, 16, 10, 10, true, false, Collections.emptyMap());
    }

    public int minPlayersPerTeam() {
        return minPlayersPerTeam;
    }

    public int maxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public int secondsToStart() {
        return secondsToStart;
    }

    public int secondsToEndMatch() {
        return secondsToEndMatch;
    }

    public void setMinPlayersPerTeam(int minPlayersPerTeam) {
        this.minPlayersPerTeam = minPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public void setSecondsToStart(int secondsToStart) {
        this.secondsToStart = secondsToStart;
    }

    public void setSecondsToEndMatch(int secondsToEndMatch) {
        this.secondsToEndMatch = secondsToEndMatch;
    }

    public boolean disableTeamSelection() {
        return disableTeamSelection;
    }

    public void setDisableTeamSelection(boolean disableTeamSelection) {
        this.disableTeamSelection = disableTeamSelection;
    }

    public boolean enableRandomTeamJoin() {
        return randomTeamJoin;
    }

    public void setRandomTeamJoin(boolean randomTeamJoin) {
        this.randomTeamJoin = randomTeamJoin;
    }

    public Map<Integer, Integer> phaseDurations() {
        return phaseDurations;
    }

    public int getPhaseDuration(int phase) {
        return phaseDurations.getOrDefault(phase, (int) Rules.DEFAULT_PHASE_DURATION.getSeconds());
    }

    @Override
    public Rules copy() {
        return new Rules(
                minPlayersPerTeam,
                maxPlayersPerTeam,
                secondsToStart,
                secondsToEndMatch,
                randomTeamJoin,
                disableTeamSelection,
                new HashMap<>(phaseDurations)
        );
    }
}
