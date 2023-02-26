package com.github.ynverxe.dtn.listener;

import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.event.player.PlayerDamageByEntityEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.event.player.PlayerDamageEvent;
import org.bukkit.event.Listener;

public class MatchPlayerDamageListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void checkDamageOutsideGame(PlayerDamageEvent event) {
        event.setCancelled(event.matchPlayer() == null);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void cancelFriendlyFire(PlayerDamageByEntityEvent event) {
        final MatchPlayer damager = event.attackerPlayer();
        final MatchPlayer damaged = event.matchPlayer();
        if (damager == null || damaged == null) {
            return;
        }
        event.setCancelled(damager.isTeamFriend(damaged));
    }
}
