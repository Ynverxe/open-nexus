package com.github.ynverxe.dtn.listener;

import com.github.ynverxe.dtn.event.player.PlayerFalseDeathEvent;
import org.bukkit.entity.Player;
import com.github.ynverxe.dtn.event.player.PlayerDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.player.PlayerDamageByEntityEvent;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;

public class EntityDamageListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        APlayer player = APlayer.resolveEntity(event.getEntity());
        if (player == null) {
            return;
        }

        Entity damager = event.getDamager();
        PlayerDamageByEntityEvent playerDamageEvent = new PlayerDamageByEntityEvent(player, damager, event);
        Bukkit.getPluginManager().callEvent(playerDamageEvent);

        MatchPlayer matchPlayer = player.toMatchRepresent().orElse(null);
        if (matchPlayer == null || event.isCancelled()) {
            return;
        }

        matchPlayer.lastDamager().setValue(damager);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        APlayer player = APlayer.resolveEntity(entity);
        if (player == null) {
            return;
        }

        PlayerDamageEvent playerDamageEvent = new PlayerDamageEvent(player, event);
        Bukkit.getPluginManager().callEvent(playerDamageEvent);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkDeathOnDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        double damage = event.getDamage();
        Player player = (Player)entity;
        APlayer aPlayer = APlayer.resolvePlayer(player);

        if (player.getHealth() - damage <= 0.0) {
            event.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new PlayerFalseDeathEvent(aPlayer));
        }
    }
}
