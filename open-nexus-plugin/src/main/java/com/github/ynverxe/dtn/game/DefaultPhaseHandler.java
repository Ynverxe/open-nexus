package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.model.data.RegenerativeBlockData;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;
import com.github.ynverxe.dtn.metadata.DTNMetadataStore;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.translation.Messenger;
import com.github.ynverxe.dtn.match.phase.PhaseHandler;

public class DefaultPhaseHandler implements PhaseHandler {
    private final Messenger messenger;
    private final GameInstanceImpl gameInstance;
    private final Match match;
    
    public DefaultPhaseHandler(GameInstance gameInstance, Match match) {
        this.messenger = DestroyTheNexus.instance().messenger();
        this.gameInstance = (GameInstanceImpl)gameInstance;
        this.match = match;
    }
    
    public int numbOfPhases() {
        return 5;
    }
    
    public void handlePhaseStart(int phaseNumber) {
        switch (phaseNumber) {
            case 1: {
                this.handleFirstPhase();
                break;
            }
            case 2: {
                this.handleSecondPhase();
                break;
            }
            case 3: {
                this.handleThirdPhase();
                break;
            }
            case 4: {
                this.handleFourthPhase();
                break;
            }
            case 5: {
                this.handleFifthPhase();
                break;
            }
        }
    }
    
    private void handleFirstPhase() {
        this.messenger.dispatchResource(this.gameInstance, DefaultTranslationContainer.FIRST_PHASE_MESSAGE);
    }
    
    private void handleSecondPhase() {
        DTNMetadataStore metadata = this.match.metadata();
        metadata.set("invincible-nexus", false);
        this.messenger.dispatchResource(this.gameInstance, DefaultTranslationContainer.SECOND_PHASE_MESSAGE);
    }
    
    private void handleThirdPhase() {
        this.messenger.dispatchResource(this.gameInstance, DefaultTranslationContainer.THIRD_PHASE_MESSAGE);

        for (Location location : this.match.runningMap().specialDiamonds()) {
            Block block = location.getBlock();
            block.setType(Material.DIAMOND_ORE);

            RegenerativeBlockData regenerativeBlockData = new RegenerativeBlockData()
                    .withRegenTime(match.runningMap().diamondsRegenTime());

            regenerativeBlockData.apply(block);
        }
    }
    
    private void handleFourthPhase() {
        this.messenger.dispatchResource(this.gameInstance, DefaultTranslationContainer.FOURTH_PHASE_MESSAGE);

        for (PreparedBoss boss : this.match.runningMap().bosses()) {
            if (boss.wasSpawned()) {
                continue;
            }

            boss.spawnEntity();
        }
    }
    
    private void handleFifthPhase() {
        this.messenger.dispatchResource(this.gameInstance, DefaultTranslationContainer.FIFTH_PHASE_MESSAGE);

        DTNMetadataStore metadata = this.match.metadata();
        metadata.set("nexus-damage-value", 2);
    }
}
