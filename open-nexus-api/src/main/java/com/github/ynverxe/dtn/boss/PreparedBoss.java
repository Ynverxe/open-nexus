package com.github.ynverxe.dtn.boss;

import com.github.ynverxe.dtn.model.data.BossModel;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public final class PreparedBoss {

    private final BossModel model;
    private final Supplier<LivingEntity> entitySpawner;
    private UUID entityId;

    public PreparedBoss(@NotNull BossModel model, @NotNull Supplier<LivingEntity> entitySpawner) {
        this.model = model;
        this.entitySpawner = entitySpawner;
    }

    public @NotNull BossModel model() {
        return model;
    }

    public @NotNull UUID entityId() {
        return entityId;
    }

    public boolean wasSpawned() {
        return entityId != null;
    }

    public @NotNull LivingEntity spawnEntity() throws UnsupportedOperationException {
        if (entityId != null) {
            throw new UnsupportedOperationException("Cannot respawn entity");
        }

        LivingEntity entity = entitySpawner.get();
        entityId = entity.getUniqueId();
        return entity;
    }
}
