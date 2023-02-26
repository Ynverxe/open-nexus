package com.github.ynverxe.dtn.listener;

import com.github.ynverxe.dtn.map.BlockRegenerator;
import com.github.ynverxe.dtn.model.data.BlockLoot;
import com.github.ynverxe.dtn.model.data.MatchMapBlockData;
import com.github.ynverxe.dtn.model.data.RegenerativeBlockData;
import org.bukkit.World;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.team.TeamColor;
import java.util.Map;
import com.github.ynverxe.dtn.map.MatchMap;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.Location;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.team.Team;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreakWhenDisqualify(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MatchPlayer matchPlayer = MatchPlayer.fromEntity(event.getPlayer());
        if (matchPlayer == null) {
            return;
        }

        if (matchPlayer.disqualified()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onNexusBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final MatchPlayer matchPlayer = MatchPlayer.fromEntity(event.getPlayer());
        if (matchPlayer == null) {
            return;
        }
        Location location = event.getBlock().getLocation();
        Match match = matchPlayer.match();
        MatchMap matchMap = match.runningMap();
        Map<TeamColor, Team> teams = match.teams();
        for (Team value : teams.values()) {
            Nexus nexus = value.nexus();
            if (!nexus.location().equals(location)) {
                continue;
            }
            if (!nexus.isAlive()) {
                matchPlayer.renderResource(DefaultTranslationContainer.NEXUS_IS_DEAD);
                event.setCancelled(true);
                return;
            }
            if (!value.isMember(matchPlayer)) {
                int damage = match.metadata().get("nexus-damage-value", 1);
                nexus.hit(matchPlayer, damage);
            } else {
                matchPlayer.renderResource(DefaultTranslationContainer.PLAYER_HIT_HIS_OWN_NEXUS);
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void performDataOnBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MatchPlayer matchPlayer = MatchPlayer.fromEntity(event.getPlayer());
        if (matchPlayer == null) {
            return;
        }

        Match match = matchPlayer.match();
        MatchMap matchMap = match.runningMap();
        BlockRegenerator blockRegenerator = matchMap.blockRegenerator();
        Block block = event.getBlock();

        if (blockRegenerator.regenerationProcessAt(block.getLocation()) != null) {
            event.setCancelled(true);
            return;
        }

        MatchMapBlockData blockData = MatchMapBlockData.fromBlock(event.getBlock());
        if (blockData == null) {
            return;
        }

        Location location = block.getLocation();

        BlockLoot blockLoot = blockData.blockLootFactory().createBlockLoot(matchPlayer.bukkitPlayer(), block);
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        World world = block.getWorld();

        for (ItemStack value : playerInventory.addItem(blockLoot.items().toArray(new ItemStack[0])).values()) {
            world.dropItemNaturally(location, value);
        }

        player.giveExp(blockLoot.experience());
        player.giveExpLevels(blockLoot.experienceLevels());
        Material substitute = Material.AIR;

        if (blockData instanceof RegenerativeBlockData) {
            RegenerativeBlockData regenerativeBlockData = (RegenerativeBlockData)blockData;
            substitute = regenerativeBlockData.substitute();
            blockRegenerator.addBlockToQueue(block.getLocation(), regenerativeBlockData.regenTime());
        }

        block.setType(substitute);
        event.setCancelled(true);
    }
}
