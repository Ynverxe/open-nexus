package com.github.ynverxe.dtn.listener;

import com.grinderwolf.swm.plugin.SWMPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.Listener;

public class SWMEnableListener implements Listener {
    private final Runnable runnable;
    
    public SWMEnableListener(Runnable runnable) {
        this.runnable = runnable;
    }
    
    @EventHandler
    public void onSWMEnable(PluginEnableEvent event) {
        if (!(event.getPlugin() instanceof SWMPlugin)) {
            return;
        }
        
        this.runnable.run();
    }
}