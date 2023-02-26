package com.github.ynverxe.dtn.command;

import java.util.Arrays;
import java.util.ArrayList;

import com.github.ynverxe.dtn.dimension.DimensionModel;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import com.github.ynverxe.dtn.dimension.edition.EditionInstanceManager;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.PropertiesContainerFactory;
import java.util.Date;

import com.github.ynverxe.dtn.world.WorldHelper;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.annotation.CSender;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import java.util.List;
import java.text.SimpleDateFormat;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.CommandClass;
import org.bukkit.World;

@Command(names = "dimensions")
public class DimensionManagementCommand implements CommandClass {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
    private static final String DIMENSION_CREATED = "&6Dimension[&b%s&6, &e%s&6] &acreated!";
    private static final String ID_IS_ALREADY_IN_USE = "&cThe id '%s' is already in use by another dimension.";
    private static final String INVALID_WORLD_NAME = "&cWorld '%s' is not found.";
    private static final String DIMENSION_DELETED = "&aDimension '&c%s&a' removed.";
    private static final String DIMENSION_REBUILD_SUCCESS = "&bDimension rebuild successfully, new Dimension: %s";
    private static final String NOT_EDITING_A_DIMENSION = "&cYou're not editing a dimension.";
    private static final String ALREADY_EDITING = "&cYou're already editing a dimension.";
    private static final String NO_EDITOR_HANDLER = "&cNo editor handler found for the type: %s";
    private static final String EDITING_DIMENSION = "&aYou're now editing the &b'%s' &adimension";
    private static final String INVALID_TYPE_NAME = "&cInvalid dimension type name '%s'. Type /dimensions types to list all available dimension types.";
    private static final List<String> DIMENSION_INFO_FORMAT = Arrays.asList("Name: <name>", "Type name: <typeName>", "Worlds: <worlds>", "Creation date: <creationDate>");
    private final DimensionManager dimensionManager;
    
    public DimensionManagementCommand() {
        DestroyTheNexus destroyTheNexus = DestroyTheNexus.instance();
        this.dimensionManager = destroyTheNexus.dimensionManager();
    }
    
    @Command(names = "")
    public boolean commandList(@CSender APlayer aPlayer) {
        aPlayer.renderResource(DefaultTranslationContainer.DIMENSION_COMMAND_LIST);
        return true;
    }
    
    @Command(names = "create")
    public boolean createDimension(@CSender APlayer aPlayer, @OptArg String dimensionId, String typeName, @Text List<String> worldNames) {
        if (this.dimensionManager.get(dimensionId) != null) {
            aPlayer.renderResource(String.format("&cThe id '%s' is already in use by another dimension.", dimensionId));
            return true;
        }

        for (String worldName : worldNames) {
            if (!WorldHelper.instance().exists(worldName)) {
                aPlayer.renderResource(String.format("&cWorld '%s' is not found.", worldName));
                return true;
            }
        }

        PropertiesContainerFactory<?> factory = PropertiesContainerFactory.REGISTRY.get(typeName);

        if (factory == null) {
            aPlayer.renderResource(String.format("&cInvalid dimension type name '%s'. Type /dimensions types to list all available dimension types.", typeName));
            return true;
        }

        DimensionModel.Named dimensionModel = new DimensionModel.Named(
                dimensionId,
                worldNames,
                typeName,
                factory.createNewProperties(), new Date()
        );

        this.dimensionManager.createDimensions(dimensionModel);
        aPlayer.renderResource(String.format("&6Dimension[&b%s&6, &e%s&6] &acreated!", dimensionId, typeName));
        return true;
    }
    
    @Command(names = "remove")
    public boolean removeDimension(@CSender APlayer aPlayer, Dimension dimension, boolean deleteData) {
        String name = dimension.name();
        this.dimensionManager.removeDimension(name);

        if (deleteData) {
            CompletableFuture.runAsync(() -> this.dimensionManager.dimensionDataRepository().delete(name));
        }

        aPlayer.renderResource(String.format("&aDimension '&c%s&a' removed.", name));
        return true;
    }
    
    @Command(names = "rebuild")
    public boolean rebuildDimension(@CSender APlayer aPlayer, Dimension dimension, String newName, String newTypeName) {
        DimensionModel.Named dimensionModel = DimensionModel.fromDimension(dimension).withName(dimension.name());
        dimension.terminate();

        Dimension newDimension = this.dimensionManager.createDimensions(dimensionModel).get(0);
        aPlayer.renderResource(String.format("&bDimension rebuild successfully, new Dimension: %s", "(" + newName + ", " + newTypeName + ")"));
        return true;
    }
    
    @Command(names = "info")
    public boolean dimensionInfo(@CSender APlayer aPlayer, Dimension dimension) {
        if (this.dimensionManager.cachedSize() == 0) {
            aPlayer.renderResource("&cNo dimensions to show :(");
            return true;
        }

        for (String entry : DimensionManagementCommand.DIMENSION_INFO_FORMAT) {
            String message = entry.replace("<name>", dimension.name())
                    .replace("<typeName>", dimension.typeName())
                    .replace("<worlds>", (dimension.worldContainer().worlds().stream().map(World::getName)
                            .collect(Collectors.toList())).toString())
                    .replace("<creationDate>", DimensionManagementCommand.DATE_FORMAT.format(dimension.creationDate()));
            aPlayer.renderResource(message);
        }

        return true;
    }
    
    @Command(names = "list")
    public boolean listDimensions(@CSender APlayer aPlayer) {
        if (this.dimensionManager.cachedSize() == 0) {
            aPlayer.renderResource("&cNo dimensions to show :(");
            return true;
        }

        for (Dimension value : this.dimensionManager.values()) {
            this.dimensionInfo(aPlayer, value);
        }

        return true;
    }
    
    @Command(names = "list-type")
    public boolean listDimensions(@CSender APlayer aPlayer, String requestedDimensionType) {
        List<Dimension> dimensions = this.dimensionManager.values().stream().filter(dimension -> dimension.typeName().equals(requestedDimensionType))
                .collect(Collectors.toList());

        if (dimensions.size() == 0) {
            aPlayer.renderResource("&cNo dimensions to show :(");
            return true;
        }

        for (Dimension value : dimensions) {
            this.dimensionInfo(aPlayer, value);
        }

        return true;
    }
    
    @Command(names = { "configure" })
    public boolean configureDimension(@CSender APlayer player, Dimension dimension) {
        if (player.editionInstance() != null) {
            player.renderResource("&cYou're already editing a dimension.");
            return true;
        }

        EditionInstanceManager editionInstanceManager = EditionInstanceManager.instance();
        try {
            editionInstanceManager.configureDimension(player, dimension);
        } catch (IllegalArgumentException e) {
            player.renderResource(String.format("&cNo editor handler found for the type: %s", dimension.typeName()));
            return true;
        }

        return true;
    }
    
    @Command(names = "save")
    public boolean saveEdition(@CSender final APlayer player) {
        EditionInstance editionInstance = player.editionInstance();
        if (editionInstance == null) {
            player.renderResource("&cYou're not editing a dimension.");
            return true;
        }

        editionInstance.save(true, true);
        player.renderResource("&aSuccess!");
        return true;
    }
    
    @Command(names = { "types" })
    public boolean listDimensionTypes(@CSender APlayer player) {
        for (PropertiesContainerFactory<?> factory : PropertiesContainerFactory.REGISTRY.values()) {
            String name = factory.name();
            List<String> desc = new ArrayList<>(factory.description());

            player.renderResource(("&a" + name));
            desc.replaceAll(s -> "&b" + s);
            player.renderResource(desc);
        }

        return true;
    }
}
