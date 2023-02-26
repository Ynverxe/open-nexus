package com.github.ynverxe.dtn.context;

import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.dtn.world.Position;
import com.github.ynverxe.dtn.world.WorldContainer;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class DimensionParsingContext {

    private final @NotNull Dimension dimension;
    private final @NotNull PropertiesContainer propertiesContainer;
    protected final @NotNull WorldContainer worldContainer;

    public DimensionParsingContext(@NotNull Dimension dimension, @NotNull WorldContainer worldContainer) {
        this.dimension = dimension;
        this.propertiesContainer = dimension.properties();
        this.worldContainer = worldContainer;
    }

    public DimensionParsingContext(@NotNull Dimension dimension, @NotNull String worldPrefix) {
        this.dimension = dimension;
        this.propertiesContainer = dimension.properties();
        this.worldContainer = dimension.cloneWorlds(worldPrefix);
    }

    public @NotNull WorldContainer getWorldContainer() {
        return worldContainer;
    }

    public @NotNull Dimension getDimension() {
        return dimension;
    }

    public @NotNull PropertiesContainer getPropertiesContainer() {
        return propertiesContainer;
    }

    protected Location createLocationBySchemeName(NamedPosition position) {
        return createLocationBySchemeName(position.worldName(), position);
    }

    protected Location createLocationBySchemeName(String name, Position position) {
        World world = worldContainer.getWorldBySchemeName(name);

        return position.toLocation(world);
    }

    protected Location createLocationBySchemeName(String name, ModelDataTree tree) {
        Position position = Position.PARSER.parseDataValue(new ModelDataValue(tree));

        return createLocationBySchemeName(name, position);
    }

    protected Location createLocationBySchemeName(ModelDataTree tree) {
        Position position = Position.PARSER.parseDataValue(new ModelDataValue(tree));

        return createLocationBySchemeName(tree.getValue("world").asString(), position);
    }
}