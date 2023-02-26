package com.github.ynverxe.dtn.world;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.structured.data.ModelDataHolder;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataTreeImpl;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface Position extends ModelDataHolder {

    Position ZERO = new PositionImpl(0, 0, 0);

    ModelDataParser<Position> PARSER = modelDataValue -> {
        ModelDataTree tree = modelDataValue.asTree();
        return new PositionImpl(
                tree.getValue("x").asDouble(),
                tree.getValue("y").asDouble(),
                tree.getValue("z").asDouble(),
                tree.getValue("yaw").asFloat(),
                tree.getValue("pitch").asFloat()
        );
    };

    double x();

    double y();

    double z();

    float yaw();

    float pitch();

    @NotNull Position normalizeAsBlock();

    @NotNull Position add(double p0, double p1, double p2, float p3, float p4);

    @NotNull Position add(double p0, double p1, double p2);

    @NotNull Position subtract(double p0, double p1, double p2, float p3, float p4);

    @NotNull Position subtract(double p0, double p1, double p2);

    @NotNull Location toLocation(@NotNull World p0);

    @NotNull Position copy();

    default @NotNull NamedPosition toNamedLocation(@NotNull World aWorld) {
        return ofLocation(toLocation(aWorld));
    }

    @Override
    default ModelDataValue toModelData() {
        ModelDataTree tree = new ModelDataTreeImpl();

        tree.setValue("x", x());
        tree.setValue("y", y());
        tree.setValue("z", z());
        tree.setValue("yaw", yaw());
        tree.setValue("pitch", pitch());

        return new ModelDataValue(tree);
    }

    static @NotNull NamedPosition ofLocation(@NotNull Location location) {
        return new NamedPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName());
    }
}
