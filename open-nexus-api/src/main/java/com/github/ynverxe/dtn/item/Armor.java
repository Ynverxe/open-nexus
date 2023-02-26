package com.github.ynverxe.dtn.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.BiConsumer;

public enum Armor {
    HELMET(PlayerInventory::setHelmet),
    CHEST_PLATE(PlayerInventory::setChestplate),
    LEGGINGS(PlayerInventory::setLeggings),
    BOOTS(PlayerInventory::setBoots);

    private final BiConsumer<PlayerInventory, ItemStack> itemApplier;

    Armor(BiConsumer<PlayerInventory, ItemStack> itemApplier) {
        this.itemApplier = itemApplier;
    }

    public void apply(PlayerInventory playerInventory, ItemStack itemStack) {
        itemApplier.accept(playerInventory, itemStack);
    }

    public void apply(Player player, ItemStack itemStack) {
        itemApplier.accept(player.getInventory(), itemStack);
    }
}
