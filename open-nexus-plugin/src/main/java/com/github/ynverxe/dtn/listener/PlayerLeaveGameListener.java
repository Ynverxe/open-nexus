package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.game.GameState;
import com.github.ynverxe.dtn.event.room.PlayerLeaveGameEvent;
import org.bukkit.event.Listener;

public class PlayerLeaveGameListener implements Listener {
    @EventHandler
    public void onRoomLeave(PlayerLeaveGameEvent event) {
        if (event.game().state() == GameState.ENDING) {
            return;
        }

        APlayer player = event.player();
        MatchPlayer matchPlayer = MatchPlayer.resolveAPlayer(player);

        if (matchPlayer == null) {
            return;
        }

        Team team = matchPlayer.team();
        if (team == null) {
            return;
        }

        if (team.isNexusAlive()) {
            matchPlayer.disqualify();
        }
    }
}
