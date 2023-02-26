package com.github.ynverxe.dtn.model.data;

import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class RegenerativeBlockData extends MatchMapBlockData {

    public static final String ALIAS = "regenerativeBlockData";

    static {
        ImplicitlyDeserializable.PARSERS.put(ALIAS, PARSER);
    }

    private @NotNull Material substitute;
    private int regenTime;

    public RegenerativeBlockData(@NotNull Material substitute, int regenTime) {
        this.substitute = substitute;
        this.withRegenTime(regenTime);
    }

    public RegenerativeBlockData() {
        this(Material.COBBLESTONE, 5);
    }

    public @NotNull Material substitute() {
        return this.substitute;
    }

    public @NotNull RegenerativeBlockData withSubstitute(@NotNull Material substitute) {
        this.substitute = substitute;
        return this;
    }

    public int regenTime() {
        return this.regenTime;
    }

    public @NotNull RegenerativeBlockData withRegenTime(int regenTime) {
        if (regenTime <= 0) {
            throw new IllegalArgumentException("regenTime <= 0");
        }

        this.regenTime = regenTime;
        return this;
    }

    @Override
    public @NotNull RegenerativeBlockData copy() {
        RegenerativeBlockData regenerativeBlockData = new RegenerativeBlockData(substitute, regenTime);
        regenerativeBlockData.withBlockLootFactory(blockLootFactory());
        return regenerativeBlockData;
    }

    @Override
    public String alias() {
        return ALIAS;
    }
}
