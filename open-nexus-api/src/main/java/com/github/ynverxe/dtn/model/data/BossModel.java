package com.github.ynverxe.dtn.model.data;

import com.github.ynverxe.dtn.data.BackedModel;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.structured.data.*;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class BossModel implements BackedModel {

    private static final ModelDataTree DEFAULTS = new ModelDataTreeImpl();

    static {
        DEFAULTS.setValue("health", 10.0);
        DEFAULTS.setValue("spawnPosition", NamedPosition.ZERO);
        DEFAULTS.setValue("extraData", new ModelDataTreeImpl());
    }

    private final @NotNull ModelDataTree tree;

    public BossModel(@NotNull ModelDataValue value) {
        this(value.asTree());
    }

    public BossModel(@NotNull ModelDataTree tree) {
        this.tree = DEFAULTS.copy();
        this.tree.consume(tree, true);
    }

    public BossModel(@NotNull String name, @NotNull String displayName, @NotNull String typeName) {
        this.tree = DEFAULTS.copy();
        tree.setValue("name", name);
        tree.setValue("displayName", displayName);
        tree.setValue("typeName", typeName);
    }

    public String name() {
        return tree.getValue("name").asString();
    }

    public String displayName() {
        return tree.getValue("displayName").asString();
    }

    public String typeName() {
        return tree.getValue("typeName").asString();
    }

    public @Nullable EntityType entityType() {
        return EntityType.fromName(tree.getValue("entityType").asString());
    }

    public double health() {
        return tree.getValue("health").asDouble();
    }

    public void setHealth(double health) {
        if (health <= 0.0) {
            throw new IllegalArgumentException("health <= 0");
        }

        tree.setValue("health", health);
    }

    public void setEntityType(@NotNull EntityType entityType) {
        tree.setValue("entityType", entityType.name());
    }

    public NamedPosition spawnLocation() {
        return NamedPosition.PARSER.parseDataValue(tree.getValue("spawnPosition"));
    }

    public void setSpawnLocation(@NotNull NamedPosition spawnLocation) {
        tree.setValue("spawnPosition", spawnLocation);
    }

    public @NotNull ModelDataTree getExtraData() {
        return tree.getValue("extraData").asTree();
    }

    public boolean isAllBasicFieldsCompleted() {
        return !NamedPosition.ZERO.equals(spawnLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BossModel bossModel = (BossModel) o;
        return tree.equals(bossModel.tree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree);
    }

    public @NotNull BossModel copy() {
        return new BossModel(tree.copy());
    }

    @Override
    public ModelDataValue getRawData() {
        return new ModelDataValue(tree);
    }
}