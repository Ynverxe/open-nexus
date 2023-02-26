package com.github.ynverxe.dtn.model.data;

import com.github.ynverxe.dtn.data.BackedModel;
import com.github.ynverxe.dtn.factory.BlockLootFactory;
import com.github.ynverxe.dtn.factory.DefaultBlockLootFactory;
import com.github.ynverxe.dtn.model.instance.DataTransferer;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataTreeImpl;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class MatchMapBlockData implements DataTransferer<MatchMapBlockData>, ImplicitlyDeserializable, BackedModel {

    public static final String ALIAS = "blockData";

    static {
        ImplicitlyDeserializable.PARSERS.put(ALIAS, PARSER);
    }

    private final @NotNull ModelDataTree tree;

    public MatchMapBlockData() {
        tree = new ModelDataTreeImpl();
        tree.setValueIfAbsent("blockLootFactoryId", () -> "default");
    }

    public MatchMapBlockData(@NotNull ModelDataValue value) {
        this(value.asTree());
    }

    public MatchMapBlockData(@NotNull ModelDataTree tree) {
        this();
        this.tree.consume(tree, true);
    }

    public BlockLootFactory blockLootFactory() {
        return BlockLootFactory.REGISTRY.getOrSupply(
                tree.getValue("blockLootFactoryId").asString(),
                DefaultBlockLootFactory::new,
                true
        );
    }

    public MatchMapBlockData withBlockLootFactory(@NotNull BlockLootFactory factory) {
        tree.setValue("blockLootFactoryId", factory.id());
        return this;
    }

    public MatchMapBlockData apply(@NotNull Block block) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DestroyTheNexus");
        MatchMapBlockData cloned = copy();
        block.setMetadata("custom_block_data", new FixedMetadataValue(plugin, cloned));
        return cloned;
    }

    @Override
    public void shareData(MatchMapBlockData recipient) {
        recipient.tree.consume(tree, true);
    }

    public @NotNull MatchMapBlockData copy() {
        return new MatchMapBlockData(tree.copy());
    }

    public static @Nullable <T extends MatchMapBlockData> T fromBlock(@NotNull Block block, @NotNull Class<T> clazz) {
        List<MetadataValue> metadataValues = block.getMetadata("custom_block_data")
                .stream()
                .filter(metadataValue -> metadataValue.value() instanceof RegenerativeBlockData)
                .collect(Collectors.toList());

        if (metadataValues.isEmpty()) {
            return null;
        }

        Object value = metadataValues.get(0).value();

        if (!clazz.isInstance(value)) {
            return null;
        }

        return clazz.cast(value);
    }

    public static @Nullable MatchMapBlockData fromBlock(@NotNull Block block) {
        return fromBlock(block, MatchMapBlockData.class);
    }

    @Override
    public ModelDataValue getRawData() {
        return new ModelDataValue(tree);
    }

    @Override
    public String alias() {
        return ALIAS;
    }
}