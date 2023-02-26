package com.github.ynverxe.dtn;

import com.github.ynverxe.dtn.dimension.DimensionModel;
import com.github.ynverxe.dtn.factory.MatchMapPropertiesContainerFactory;
import com.github.ynverxe.dtn.factory.PropertiesContainerFactory;
import com.github.ynverxe.dtn.factory.RoomLobbyPropertiesContainerFactory;
import com.github.ynverxe.dtn.game.GameRoomModel;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.CommandManager;
import org.bukkit.plugin.PluginManager;
import com.github.ynverxe.dtn.papi.TeamPlaceholderExpansion;
import com.github.ynverxe.dtn.papi.GamePlaceholderExpansion;
import com.github.ynverxe.dtn.papi.APlayerPlaceholderHolderExpansion;
import com.github.ynverxe.dtn.command.DTNCommand;
import com.github.ynverxe.dtn.command.TeamCommand;
import com.github.ynverxe.dtn.command.MapEditorCommand;
import com.github.ynverxe.dtn.command.GameManagementCommand;
import com.github.ynverxe.dtn.command.DimensionManagementCommand;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import com.github.ynverxe.dtn.command.DTNModule;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import com.github.ynverxe.dtn.listener.TeamDisqualifyListener;
import com.github.ynverxe.dtn.listener.PlayerQuitListener;
import com.github.ynverxe.dtn.listener.PlayerDisqualifyListener;
import com.github.ynverxe.dtn.listener.NexusDestroyListener;
import com.github.ynverxe.dtn.listener.NexusDamageListener;
import com.github.ynverxe.dtn.listener.BlockBreakListener;
import com.github.ynverxe.dtn.listener.PlayerRespawnListener;
import com.github.ynverxe.dtn.listener.EntityDamageListener;
import com.github.ynverxe.dtn.listener.PlayerLeaveGameListener;
import com.github.ynverxe.dtn.listener.PlayerJoinGameListener;
import com.github.ynverxe.dtn.listener.PlayerSpawnLocationListener;
import com.github.ynverxe.dtn.listener.PlayerDeathListener;
import com.github.ynverxe.dtn.listener.MatchPlayerDamageListener;
import com.github.ynverxe.dtn.listener.PlayerJoinListener;
import com.github.ynverxe.dtn.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import java.util.List;
import com.github.ynverxe.dtn.boss.BossType;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import com.github.ynverxe.dtn.game.GameManager;
import java.util.logging.Level;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.environment.DTNEnvironment;

public class DTNLauncher {

    void launch() {
        if (DTNSingletonContainer.dtnInstance() != null) {
            throw new UnsupportedOperationException("DTN was already initialized");
        }

        try {
            new DTNEnvironment(DestroyTheNexusPlugin.plugin().folder());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        DTNInitializer.initialize();
        this.postDTNInitialize();
    }
    
    void close() {
        if (DTNSingletonContainer.dtnInstance() == null) {
            return;
        }

        Logger logger = DestroyTheNexus.LOGGER;
        DimensionManager.instance().saveAll(false).whenComplete((a, b) -> logger.log(Level.INFO, "Saved dimensions"));
        GameManager.instance().saveAll(false).whenComplete((a, b) -> logger.log(Level.INFO, "Saved games"));
    }
    
    protected void postDTNInitialize() {
        initModelTypes();

        DestroyTheNexus destroyTheNexus = DestroyTheNexus.instance();
        DimensionManager dimensionManager = DimensionManager.instance();

        for (Map.Entry<String, DimensionModel> entry : dimensionManager.dimensionDataRepository().groupAll().entrySet()) {
            dimensionManager.createDimensions(entry.getValue().withName(entry.getKey()));
        }

        GameManager gameManager = GameManager.instance();
        for (GameRoomModel value : gameManager.gameRoomModelRepository().groupAll().values()) {
            gameManager.createRoom(value);
        }

        PluginManager pluginManager = Bukkit.getPluginManager();
        DestroyTheNexusPlugin plugin = DestroyTheNexusPlugin.plugin();
        Scheduler scheduler = Scheduler.dtnScheduler();

        scheduler.executeTaskConstantly(destroyTheNexus.gameInstanceUpdater(), 0, 20, false);
        scheduler.executeTaskConstantly(destroyTheNexus.boardManager(), 0, 1, true);
        scheduler.executeTaskConstantly(destroyTheNexus.tickableRegistry(), 0, 1, true);

        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
        pluginManager.registerEvents(new MatchPlayerDamageListener(), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(), plugin);
        pluginManager.registerEvents(new PlayerSpawnLocationListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinGameListener(), plugin);
        pluginManager.registerEvents(new PlayerLeaveGameListener(), plugin);
        pluginManager.registerEvents(new EntityDamageListener(), plugin);
        pluginManager.registerEvents(new PlayerRespawnListener(), plugin);
        pluginManager.registerEvents(new BlockBreakListener(), plugin);
        pluginManager.registerEvents(new NexusDamageListener(), plugin);
        pluginManager.registerEvents(new NexusDestroyListener(), plugin);
        pluginManager.registerEvents(new PlayerDisqualifyListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new TeamDisqualifyListener(), plugin);

        CommandManager commandManager = plugin.commandManager();
        PartInjector partInjector = plugin.partInjector();

        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());
        partInjector.install(new DTNModule());

        AnnotatedCommandTreeBuilder commandTreeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);
        List<Command> dimensionCommands = commandTreeBuilder.fromClass(new DimensionManagementCommand());
        commandManager.registerCommands(dimensionCommands);

        List<Command> gameCommands = commandTreeBuilder.fromClass(new GameManagementCommand());
        commandManager.registerCommands(gameCommands);

        List<Command> mapEditorCommands = commandTreeBuilder.fromClass(new MapEditorCommand());
        commandManager.registerCommands(mapEditorCommands);

        List<Command> teamCommands = commandTreeBuilder.fromClass(new TeamCommand());
        commandManager.registerCommands(teamCommands);

        List<Command> dtnCommands = commandTreeBuilder.fromClass(new DTNCommand());
        commandManager.registerCommands(dtnCommands);

        new APlayerPlaceholderHolderExpansion().register();
        new GamePlaceholderExpansion().register();
        new TeamPlaceholderExpansion().register();
    }

    protected void initModelTypes() {
        PropertiesContainerFactory.REGISTRY.registerObject(new MatchMapPropertiesContainerFactory());
        PropertiesContainerFactory.REGISTRY.registerObject(new RoomLobbyPropertiesContainerFactory());

        BossType golemBossType = new BossType(
                "golem-boss",
                Collections.singletonList("The default DTN golem boss type implementation."),
                EntityType.IRON_GOLEM
        );

        BossType witherBossType = new BossType(
                "wither-boss",
                Collections.singletonList("The default DTN wither boss type implementation."),
                EntityType.WITHER
        );

        BossType.REGISTRY.registerObject(golemBossType);
        BossType.REGISTRY.registerObject(witherBossType);
    }
}
