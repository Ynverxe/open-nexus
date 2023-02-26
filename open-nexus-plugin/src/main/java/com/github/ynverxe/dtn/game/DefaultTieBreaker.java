package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.match.Disqualifiable;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.team.Team;

import java.time.temporal.ChronoUnit;
import java.util.List;
import com.github.ynverxe.dtn.DestroyTheNexusPlugin;
import com.github.ynverxe.dtn.runnable.ProgressiveNexusDamager;
import java.time.Duration;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.match.TieBreaker;

public class DefaultTieBreaker implements TieBreaker {
    @Nullable
    public Duration runTieBreak(@NotNull Match match) {
        List<Team> teams = match.nonDisqualifiedTeams();

        List<Team> disqualifiableTeams = teams.stream()
                .filter(team -> !team.isNexusAlive())
                .collect(Collectors.toList());

        teams.removeAll(disqualifiableTeams);

        if (teams.size() == 1) {
            match.handleTeamWinner(teams.get(0));
            return null;
        } else if (teams.isEmpty()) {
            match.handleTeamWinner(null);
            return null;
        } else {
            disqualifiableTeams.forEach(Disqualifiable::disqualify);
        }

        ProgressiveNexusDamager damager = new ProgressiveNexusDamager(match);
        damager.runTaskTimer(DestroyTheNexusPlugin.plugin(), 0L, 5L);

        return Duration.of(1, ChronoUnit.MINUTES);
    }
}
