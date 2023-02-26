package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        APlayer player = APlayer.resolvePlayer(event.getPlayer());
        GameInstance game = player.playingGame();

        if (game != null) {
            game.leavePlayer(player);
        }
    }
}
