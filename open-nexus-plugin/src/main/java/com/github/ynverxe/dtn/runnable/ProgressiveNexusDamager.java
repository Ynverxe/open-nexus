package com.github.ynverxe.dtn.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.ynverxe.dtn.match.Disqualifiable;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.scheduler.BukkitRunnable;

public class ProgressiveNexusDamager extends BukkitRunnable {

    private final Match match;

    public ProgressiveNexusDamager(Match match) {
        this.match = match;
    }
    
    public void run() {
        if (match.hasEnd()) {
            this.cancel();
            return;
        }

        List<Team> aliveNexusTeams = match.aliveNexuses()
                .stream()
                .map(Nexus::team)
                .collect(Collectors.toList());

        List<Team> teamsToDisqualify = new ArrayList<>();
        for (Team team : aliveNexusTeams) {
            Nexus nexus = team.nexus();

            nexus.hit(null, 1);

            if (!nexus.isAlive()) {
                teamsToDisqualify.add(nexus.team());
            }
        }

        aliveNexusTeams.removeAll(teamsToDisqualify);

        if (aliveNexusTeams.isEmpty()) {
            match.handleTeamWinner(null);
        } else {
            teamsToDisqualify.forEach(Disqualifiable::disqualify);
        }

        if (aliveNexusTeams.size() == 1) {
            this.cancel();
        }
    }
}
