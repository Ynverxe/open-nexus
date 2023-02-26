package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.event.match.MatchPlayerDisqualifyEvent;
import org.bukkit.event.Listener;

public class PlayerDisqualifyListener implements Listener {
    @EventHandler
    public void onDisqualify(MatchPlayerDisqualifyEvent event) {
        MatchPlayer matchPlayer = event.matchPlayer();
        Team team = matchPlayer.team();

        if (team == null) {
            return;
        }

        // when team is DISQUALIFIED this disqualifies all non disqualified members
        // evict second disqualify method call
        if (team.nonDisqualifiedMembers().isEmpty() && !team.disqualified()) {
            team.disqualify();
        }
    }
}
