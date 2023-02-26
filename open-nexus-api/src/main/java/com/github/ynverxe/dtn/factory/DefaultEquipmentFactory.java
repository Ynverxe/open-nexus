package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.item.Armor;
import com.github.ynverxe.dtn.kit.Equipment;
import com.github.ynverxe.dtn.kit.Kit;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class DefaultEquipmentFactory implements EquipmentFactory {
    @NotNull @Override
    public Equipment createEquipment(@NotNull Kit kit, @NotNull MatchPlayer matchPlayer) {
        final Color color = matchPlayer.team().color().bukkitColor();
        final Equipment equipment = new Equipment();
        equipment.addArmorPiece(Armor.HELMET, createArmor(kit, color, Material.LEATHER_HELMET));
        equipment.addArmorPiece(Armor.CHEST_PLATE, createArmor(kit, color, Material.LEATHER_CHESTPLATE));
        equipment.addArmorPiece(Armor.LEGGINGS, createArmor(kit, color, Material.LEATHER_LEGGINGS));
        equipment.addArmorPiece(Armor.BOOTS, createArmor(kit, color, Material.LEATHER_BOOTS));
        equipment.addItem(0, kit.applyKitMark(new ItemStack(Material.WOOD_SWORD)));
        equipment.addItem(0, kit.applyKitMark(new ItemStack(Material.WOOD_AXE)));
        equipment.addItem(0, kit.applyKitMark(new ItemStack(Material.WOOD_PICKAXE)));
        return equipment;
    }

    private ItemStack createArmor(Kit kit, Color color, Material material) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();

        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta(leatherArmorMeta);
        return kit.applyKitMark(itemStack);
    }
}
