package com.github.ynverxe.dtn.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PositionImpl implements Position {

    protected final double x, y, z;
    protected final float yaw, pitch;

    public PositionImpl(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PositionImpl(double x, double y, double z) {
        this(x, y, z, 0.0f, 0.0f);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @NotNull @Override
    public Position normalizeAsBlock() {
        return new PositionImpl(NumberConversions.floor(x()), NumberConversions.floor(y()), NumberConversions.floor(z()), yaw, pitch);
    }

    @NotNull @Override
    public Position add(double x, double y, double z, float yaw, float pitch) {
        return new PositionImpl(x + x, y + y, z + z, yaw + yaw, pitch + pitch);
    }

    @NotNull @Override
    public Position add(double x, double y, double z) {
        return add(x, y, z, 0.0f, 0.0f);
    }

    @NotNull @Override
    public Position subtract(double x, double y, double z, float yaw, float pitch) {
        return new PositionImpl(this.x - x, this.y - y, this.z - z, this.yaw - yaw, this.pitch - pitch);
    }

    @NotNull @Override
    public Position subtract(double x, double y, double z) {
        return subtract(x, y, z, 0.0f, 0.0f);
    }

    @NotNull @Override
    public Location toLocation(@NotNull World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull Position copy() {
        return new PositionImpl(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "PositionImpl{x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + '}';
    }

    public @NotNull String format() {
        return format(null);
    }

    public @NotNull String format(@Nullable String world) {
        String formattedPosition = String.format("%1$f, %2$f, %3$f, %4$f, %5$f", x, y, z, yaw, pitch);
        if (world != null) {
            formattedPosition = world + ", " + formattedPosition;
        }
        return formattedPosition;
    }
}
