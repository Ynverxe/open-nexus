package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.dtn.currency.ComposedCurrencyPrice;
import com.github.ynverxe.dtn.factory.EquipmentFactory;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.translation.resource.ResourceReference;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleKit implements Kit {
    final List<Listener> listeners;
    private final String name;
    private final KitDescription kitDescription;
    private final PlayerPreparer playerPreparer;
    private final EquipmentFactory equipmentFactory;
    private final ComposedCurrencyPrice composedCurrencyPrice;
    BukkitTask bukkitTask;
    Plugin kitOwner;

    SimpleKit(String name, KitDescription kitDescription, PlayerPreparer playerPreparer, EquipmentFactory equipmentFactory) {
        composedCurrencyPrice = new ComposedCurrencyPrice();
        listeners = new ArrayList<>();
        this.name = name;
        this.kitDescription = kitDescription;
        this.playerPreparer = playerPreparer;
        this.equipmentFactory = equipmentFactory;
    }

    @NotNull @Override
    public String name() {
        return name;
    }

    @NotNull @Override
    public ResourceReference<String> nameAsResourceReference() {
        return ResourceReference.create(String.class, "kit-name-" + name);
    }

    @NotNull @Override
    public KitDescription description() {
        return kitDescription;
    }

    @Override
    public void preparePlayer(@NotNull MatchPlayer matchPlayer) {
        playerPreparer.preparePlayer(this, matchPlayer);
    }

    @NotNull @Override
    public Equipment createEquipmentForPlayer(@NotNull MatchPlayer matchPlayer) {
        final Equipment equipment = equipmentFactory.createEquipment(this, matchPlayer);
        equipment.armorPieces().replaceAll((k, v) -> applyKitMark(v));
        equipment.inventoryItems().replaceAll((k, v) -> applyKitMark(v));
        return equipment;
    }

    @Override
    public boolean isKitMarkApplied(@NotNull ItemStack itemStack) {
        final NBTItem nbtItem = new NBTItem(itemStack);
        final String kitMark = nbtItem.getString("kit-mark");
        return name.equals(kitMark);
    }

    @NotNull @Override
    public ItemStack applyKitMark(@NotNull ItemStack itemStack) {
        final NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("kit-mark", name);
        return nbtItem.getItem();
    }

    @NotNull @Override
    public ComposedCurrencyPrice price() {
        return composedCurrencyPrice;
    }

    @NotNull @Override
    public BukkitTask bukkitTask() {
        return bukkitTask;
    }

    @NotNull @Override
    public List<Listener> listeners() {
        return listeners;
    }

    @NotNull @Override
    public Plugin kitOwner() {
        return kitOwner;
    }
}
