package com.github.ynverxe.dtn.listener;

import com.github.ynverxe.dtn.event.match.NexusDestroyEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.dtn.effect.DTNDefaultEffects;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.event.match.NexusDamageEvent;
import org.bukkit.event.Listener;

public class NexusDamageListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onNexusHit(@NotNull NexusDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        DTNDefaultEffects.NEXUS_AGONY_EFFECT.playAt(event.nexus().location());
        Match match = event.match();
        Nexus nexus = event.nexus();
        Team nexusTeam = nexus.team();

        if (event.reason() != NexusDamageEvent.Reason.PLAYER) {
            return;
        }

        MatchPlayer matchPlayer = event.damager();
        String hitterName = matchPlayer.coloredName();

        for (MatchPlayer somePlayer : match.players().values()) {

            String nexusName = somePlayer.findTranslationResource(nexusTeam.color().nexusNameReference(), null);
            somePlayer.renderResource(DefaultTranslationContainer.PLAYER_HIT_NEXUS.replacing("<player>", hitterName).replacing("<nexus>", nexusName).replacing("<remainingHealth>", (nexus.remainingHealth() - event.damage())));
        }
    }
    
    @EventHandler
    public void onNexusDestroy(NexusDestroyEvent event) {
        Team nexusTeam = event.nexus().team();
        for (MatchPlayer teamMember : nexusTeam.members()) {
            teamMember.renderResource(DefaultTranslationContainer.YOUR_NEXUS_HAS_BEEN_DESTROYED);
        }

        if (event.reason() != NexusDestroyEvent.Reason.PLAYER) {
            return;
        }

        String hitterName = event.damager().coloredName();
        Match match = event.match();
        for (MatchPlayer somePlayer : match.players().values()) {
            String nexusName = somePlayer.findTranslationResource(nexusTeam.color().nexusNameReference(), null);
            somePlayer.renderResource(DefaultTranslationContainer.GLOBAL_NEXUS_DEATH.replacing("<nexus>", nexusName).replacing("<player>", hitterName));
        }
    }
}
