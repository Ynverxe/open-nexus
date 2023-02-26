package com.github.ynverxe.dtn.model.data;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class BlockLoot {

    private final List<ItemStack> items;
    private final int experience;
    private final int experienceLevels;

    public BlockLoot(List<ItemStack> items, int experience, int experienceLevels) {
        this.items = items;
        this.experience = experience;
        this.experienceLevels = experienceLevels;
    }

    public List<ItemStack> items() {
        return items;
    }

    public int experience() {
        return experience;
    }

    public int experienceLevels() {
        return experienceLevels;
    }
}
