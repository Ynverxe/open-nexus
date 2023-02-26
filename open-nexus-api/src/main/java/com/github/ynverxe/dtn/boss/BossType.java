package com.github.ynverxe.dtn.boss;

import com.github.ynverxe.dtn.registry.ObjectRegistry;
import com.github.ynverxe.dtn.registry.Registrable;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class BossType implements Registrable {

    public static final ObjectRegistry<BossType> REGISTRY = ObjectRegistry.create("BossTypeRegistry");

    private final @NotNull String typeName;
    private final @NotNull List<String> description;
    private final @Nullable EntityType entityType;

    public BossType(@NotNull String typeName, @NotNull List<String> description, @Nullable EntityType entityType) {
        this.typeName = typeName;
        this.description = description;
        this.entityType = entityType;
    }

    public String typeName() {
        return typeName;
    }

    public List<String> description() {
        return description;
    }

    @Override
    public String id() {
        return typeName;
    }

    public @Nullable EntityType entityType() {
        return entityType;
    }
}
