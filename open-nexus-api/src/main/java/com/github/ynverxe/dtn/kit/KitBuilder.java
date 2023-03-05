package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.dtn.currency.ComposedCurrencyPrice;
import com.github.ynverxe.dtn.currency.CurrencyPrice;
import com.github.ynverxe.dtn.factory.EquipmentFactory;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class KitBuilder {
    final String name;
    final ComposedCurrencyPrice composedCurrencyPrice;
    final Plugin plugin;
    final List<Function<Kit, Listener>> listenerFactories;
    private final AbstractKitRegistry kitRegistry;
    EquipmentFactory equipmentFactory;
    PlayerPreparer playerPreparer;
    Consumer<Kit> kitTaskAction;
    ResourceReference<String> kitDescriptionLore;

    KitBuilder(@NotNull String name, @NotNull Plugin plugin, AbstractKitRegistry kitRegistry) {
        composedCurrencyPrice = new ComposedCurrencyPrice();
        listenerFactories = new ArrayList<>();
        equipmentFactory = ((kit, matchPlayer) -> new Equipment());
        playerPreparer = ((kit, aPlayer) -> {
        });
        kitDescriptionLore = ResourceReference.createWithAuxiliary(String.class, Collections::emptyList);
        this.name = name;
        this.plugin = plugin;
        this.kitRegistry = kitRegistry;
    }

    public @NotNull KitBuilder setEquipmentFactory(EquipmentFactory equipmentFactory) {
        this.equipmentFactory = equipmentFactory;
        return this;
    }

    @SafeVarargs
    public final @NotNull KitBuilder addListenerFactories(@NotNull Function<Kit, Listener>... listenerFactories) {
        this.listenerFactories.addAll(Arrays.asList(listenerFactories));
        return this;
    }

    public @NotNull KitBuilder addPrices(@NotNull CurrencyPrice... currencyPrices) {
        for (CurrencyPrice currencyPrice : currencyPrices) {
            composedCurrencyPrice.addPrice(currencyPrice);
        }
        return this;
    }

    public @NotNull KitBuilder setKitDescriptionLore(@NotNull ResourceReference<String> kitDescriptionLore) {
        this.kitDescriptionLore = kitDescriptionLore;
        return this;
    }

    public @NotNull KitBuilder setTaskAction(@NotNull Consumer<Kit> kitTaskAction) {
        this.kitTaskAction = kitTaskAction;
        return this;
    }

    public @NotNull KitBuilder setPlayerPreparer(PlayerPreparer playerPreparer) {
        this.playerPreparer = playerPreparer;
        return this;
    }

    public @NotNull Kit build() {
        PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
        KitDescription kitDescription = new KitDescription(kitDescriptionLore, pluginDescriptionFile.getFullName(), pluginDescriptionFile.getAuthors().toString());
        SimpleKit kit = new SimpleKit(name, kitDescription, playerPreparer, equipmentFactory);
        kitRegistry.completeKitBuild(kit, this);
        return kit;
    }
}
