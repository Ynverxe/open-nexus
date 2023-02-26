package com.github.ynverxe.dtn;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import org.bukkit.plugin.PluginManager;
import com.github.ynverxe.dtn.listener.SWMEnableListener;
import org.bukkit.Bukkit;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DestroyTheNexusPlugin extends JavaPlugin {

    private DTNLauncher launcher;
    private final Runnable launcherTrigger;
    private final CommandManager commandManager;
    private final PartInjector partInjector;
    
    public DestroyTheNexusPlugin() {
        this.launcherTrigger = () -> {
            try {
                DestroyTheNexusPlugin.this.launcher.launch();
            } catch (Exception e) {
                setEnabled(false);
                throw new RuntimeException(e);
            }
        };
        this.commandManager = new BukkitCommandManager("dtn");
        this.partInjector = PartInjector.create();
    }
    
    public void onLoad() {
        this.setLauncher(new DTNLauncher());
    }
    
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("SlimeWorldManager")) {
            this.launcherTrigger.run();
        } else {
            pluginManager.registerEvents(new SWMEnableListener(this.launcherTrigger), this);
        }
    }
    
    public void onDisable() {
        this.launcher.close();
    }
    
    @NotNull
    public File folder() {
        File folder = this.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("unable to create plugin folder");
        }

        return folder;
    }
    
    @NotNull
    public CommandManager commandManager() {
        return this.commandManager;
    }
    
    @NotNull
    public PartInjector partInjector() {
        return this.partInjector;
    }
    
    protected void setLauncher(DTNLauncher launcher) {
        this.launcher = launcher;
    }
    
    @NotNull
    public static DestroyTheNexusPlugin plugin() {
        return getPlugin(DestroyTheNexusPlugin.class);
    }
}
