package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import java.util.List;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.event.match.TeamDisqualifyEvent;
import org.bukkit.event.Listener;

public class TeamDisqualifyListener implements Listener {
    @EventHandler
    public void onDisqualify(TeamDisqualifyEvent event) {
        Match match = event.match();
        List<Team> nonDisqualifiedTeams = match.nonDisqualifiedTeams();

        if (nonDisqualifiedTeams.size() == 1) {
            match.handleTeamWinner(nonDisqualifiedTeams.get(0));
        }
    }
}
