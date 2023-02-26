package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.model.data.BlockLoot;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DefaultBlockLootFactory implements BlockLootFactory {

    @NotNull @Override
    public BlockLoot createBlockLoot(@NotNull Player player, @NotNull Block block) {
        return new BlockLoot(new ArrayList<>(block.getDrops()), 0, 0);
    }

    @Override
    public @NotNull String id() {
        return "default";
    }
}
