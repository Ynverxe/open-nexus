package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.dtn.currency.ComposedCurrencyPrice;
import com.github.ynverxe.dtn.player.MatchPlayer;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Kit {
    @NotNull String name();

    @NotNull ResourceReference<String> nameAsResourceReference();

    @NotNull KitDescription description();

    void preparePlayer(@NotNull MatchPlayer p0);

    @NotNull Equipment createEquipmentForPlayer(@NotNull MatchPlayer p0);

    boolean isKitMarkApplied(@NotNull ItemStack p0);

    @NotNull ItemStack applyKitMark(@NotNull ItemStack p0);

    @NotNull ComposedCurrencyPrice price();

    @NotNull BukkitTask bukkitTask();

    @NotNull List<Listener> listeners();

    @NotNull Plugin kitOwner();

}