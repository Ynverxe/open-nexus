package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.dtn.item.Armor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Equipment {
    private final Map<Armor, ItemStack> armorPieces;
    private final Map<Integer, ItemStack> inventoryItems;

    public Equipment(Map<Armor, ItemStack> armorPieces, Map<Integer, ItemStack> inventoryItems) {
        this.armorPieces = new HashMap<>(armorPieces);
        this.inventoryItems = new HashMap<>(inventoryItems);
    }

    public Equipment() {
        this(Collections.emptyMap(), Collections.emptyMap());
    }

    public @NotNull Equipment addArmorPiece(@NotNull Armor armor, @NotNull ItemStack itemStack) {
        armorPieces.put(armor, itemStack);
        return this;
    }

    public @NotNull Equipment addItem(int slot, @NotNull ItemStack itemStack) {
        inventoryItems.put(slot, itemStack);
        return this;
    }

    public Map<Armor, ItemStack> armorPieces() {
        return armorPieces;
    }

    public Map<Integer, ItemStack> inventoryItems() {
        return inventoryItems;
    }
}
