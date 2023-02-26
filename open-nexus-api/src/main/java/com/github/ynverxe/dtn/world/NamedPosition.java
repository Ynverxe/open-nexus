package com.github.ynverxe.dtn.world;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NamedPosition extends PositionImpl {

    public static final NamedPosition ZERO = new NamedPosition(0, 0, 0, "");

    public static final ModelDataParser<NamedPosition> PARSER = value -> {
        Position position = Position.PARSER.parseDataValue(value);

        ModelDataTree tree = value.asTree();

        return new NamedPosition(
                position.x(),
                position.y(),
                position.z(),
                position.yaw(),
                position.pitch(),
                tree.getValue("world").asString()
        );
    };

    private final String name;

    public NamedPosition(double x, double y, double z, float yaw, float pitch, String name) {
        super(x, y, z, yaw, pitch);
        this.name = Objects.requireNonNull(name, "worldName");
    }

    public NamedPosition(double x, double y, double z, String name) {
        this(x, y, z, 0.0f, 0.0f, name);
    }

    @NotNull @Override
    public NamedPosition normalizeAsBlock() {
        return new NamedPosition(NumberConversions.floor(x()), NumberConversions.floor(y()), NumberConversions.floor(z()), yaw, pitch, name);
    }

    public @NotNull String worldName() {
        return name;
    }

    public @NotNull NamedPosition withName(@NotNull String worldName) {
        return new NamedPosition(x, y, z, yaw, pitch, worldName);
    }

    public World world() {
        return Bukkit.getWorld(name);
    }

    public Location toLocation() {
        return toLocation(world());
    }

    public Location toLocationBySchemeName(WorldContainer worldContainer) {
        return toLocation(worldContainer.getWorldBySchemeName(name));
    }

    @Override
    public @NotNull NamedPosition copy() {
        return new NamedPosition(x, y, z, yaw, pitch, name);
    }

    public @NotNull String formatWithWorld() {
        return format(name);
    }

    @Override
    public ModelDataValue toModelData() {
        ModelDataValue value = super.toModelData();

        value.asTree().setValue("world", name);

        return value;
    }

    public static NamedPosition fromLine(String line) {
        String[] parts = line.split(",");

        return new NamedPosition(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Float.parseFloat(parts[3]),
                Float.parseFloat(parts[4]),
                parts[5]
        );
    }
}
