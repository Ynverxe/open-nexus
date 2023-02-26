package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.model.data.BlockLoot;
import com.github.ynverxe.dtn.registry.ObjectRegistry;
import com.github.ynverxe.dtn.registry.Registrable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BlockLootFactory extends Registrable {

    ObjectRegistry<BlockLootFactory> REGISTRY = ObjectRegistry.create("BlockLootFactoryRegistry", new DefaultBlockLootFactory());

    @NotNull BlockLoot createBlockLoot(@NotNull Player p0, @NotNull Block p1);

}
