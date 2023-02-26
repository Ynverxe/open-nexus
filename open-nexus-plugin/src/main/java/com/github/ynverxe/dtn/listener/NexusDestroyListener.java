package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import com.github.ynverxe.dtn.event.match.NexusDestroyEvent;
import org.bukkit.event.Listener;

public class NexusDestroyListener implements Listener {
    @EventHandler
    public void onDestroy(NexusDestroyEvent event) {
        event.nexus().location().getBlock().setType(Material.BEDROCK);
    }
}
