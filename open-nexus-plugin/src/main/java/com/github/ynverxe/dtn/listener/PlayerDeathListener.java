package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;
import org.bukkit.entity.Player;
import com.github.ynverxe.dtn.DestroyTheNexus;
import org.bukkit.potion.PotionEffect;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.event.player.PlayerFalseDeathEvent;
import org.bukkit.event.Listener;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void handleEvent(@NotNull PlayerFalseDeathEvent event) {
        final MatchPlayer matchPlayer = event.player().toMatchRepresent().orElse(null);
        final Player player = event.bukkitPlayer();
        player.getInventory().clear();
        player.setFireTicks(0);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.setHealth(player.getMaxHealth());
        if (matchPlayer == null) {
            DestroyTheNexus.instance().sendToLobby(event.player());
            return;
        }
        final Team team = matchPlayer.team();
        final Match match = matchPlayer.match();
        final boolean playerDisqualified = matchPlayer.disqualified();
        if (playerDisqualified || team == null || !team.isNexusAlive()) {
            final Location spawnPosition = match.room().lobbySpawn();
            matchPlayer.teleport(spawnPosition);
            if (!playerDisqualified && team != null) {
                matchPlayer.disqualify();
            }
        }
        else {
            match.preparePlayer(matchPlayer, true);
        }
    }
}
