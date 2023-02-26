package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.DestroyTheNexus;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import org.bukkit.event.Listener;

public class PlayerSpawnLocationListener implements Listener {
    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        DestroyTheNexus destroyTheNexus = DestroyTheNexus.instance();
        event.setSpawnLocation(destroyTheNexus.lobbyWorld().getSpawnLocation());
    }
}
