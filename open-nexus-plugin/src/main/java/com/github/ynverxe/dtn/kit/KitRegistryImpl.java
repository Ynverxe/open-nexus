package com.github.ynverxe.dtn.kit;

import org.bukkit.plugin.PluginDescriptionFile;

import java.util.function.Consumer;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.factory.DefaultEquipmentFactory;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.bukkit.plugin.Plugin;
import com.github.ynverxe.dtn.DestroyTheNexusPlugin;
import com.github.ynverxe.dtn.DestroyTheNexus;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class KitRegistryImpl extends AbstractKitRegistry {
    
    private final Logger logger;
    private final Map<String, Kit> registeredKits;
    private final Kit defaultKit;
    
    public KitRegistryImpl() {
        this.registeredKits = new HashMap<>();
        this.logger = DestroyTheNexus.LOGGER;
        this.defaultKit = this.createKitBuilder("default", DestroyTheNexusPlugin.plugin())
                .setKitDescriptionLore(ResourceReference.create(String.class, "default-kit-description")
                        .withDefaultValue("&aThe default annihilation kit."))
                .setEquipmentFactory(new DefaultEquipmentFactory()).build();
    }
    
    @NotNull
    public KitBuilder createKitBuilder(@NotNull String kitName, @NotNull Plugin plugin) {
        return new KitBuilder(kitName, plugin, this);
    }

    public @Nullable Kit findKit(@NotNull String name) {
        return this.registeredKits.get(name);
    }
    
    @NotNull
    public List<Kit> getPluginKits(@NotNull Plugin plugin) {
        return this.registeredKits.values().stream().filter(kit -> kit.kitOwner().equals(plugin))
                .collect(Collectors.toList());
    }
    
    void completeKitBuild(SimpleKit kit, KitBuilder kitBuilder) {
        if (this.registeredKits.containsKey(kitBuilder.name)) {
            throw new IllegalArgumentException(kitBuilder.name + " is already registered.");
        }

        Plugin plugin = kitBuilder.plugin;
        kit.kitOwner = plugin;
        List<Function<Kit, Listener>> listenerFactories = kitBuilder.listenerFactories;

        for (Function<Kit, Listener> listenerFactory : listenerFactories) {
            final Listener listener = listenerFactory.apply(kit);
            Bukkit.getPluginManager().registerEvents(listener, plugin);
            kit.listeners.add(listener);
        }

        Consumer<Kit> kitTaskAction = kitBuilder.kitTaskAction;
        if (kitTaskAction != null) {
            kit.bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> kitTaskAction.accept(kit), 0L, 1L);
        }

        this.registeredKits.put(kit.name(), kit);

        PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
        this.logger.log(
                Level.INFO,
                "Kit[{0}, {1}, {2}] registered successfully!",
                new Object[] { kit.name(), pluginDescriptionFile.getFullName(), pluginDescriptionFile.getAuthors().toString() }
        );
    }
    
    @NotNull
    public Iterator<Kit> iterator() {
        return this.registeredKits.values().iterator();
    }
    
    @NotNull
    public Kit defaultKit() {
        return this.defaultKit;
    }
}
