package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.scheduler.Scheduler;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.Listener;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        APlayer aPlayer = APlayer.resolvePlayer(event.getPlayer());
        MatchPlayer matchPlayer = MatchPlayer.resolveAPlayer(aPlayer);
        GameRoom gameRoom = aPlayer.playingRoom();

        Location spawn;
        if (matchPlayer != null) {
            if (!matchPlayer.isTeamAlive()) {
                spawn = gameRoom.lobbySpawn();
            } else {
                spawn = matchPlayer.team().randomSpawn();
                Scheduler.dtnScheduler().executeTask(() -> matchPlayer.match().preparePlayer(matchPlayer, true), 20, false);
            }
        } else if (gameRoom == null) {
            DestroyTheNexus destroyTheNexus = DestroyTheNexus.instance();
            spawn = destroyTheNexus.lobbyWorld().getSpawnLocation();
            Scheduler.dtnScheduler().executeTask(() -> destroyTheNexus.sendToLobby(aPlayer), 20, false);
        } else {
            spawn = gameRoom.lobbySpawn();
        }

        event.setRespawnLocation(spawn);
    }
}
