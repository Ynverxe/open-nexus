package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.boss.BossType;
import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.exception.ValueParseException;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.model.data.BossModel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public final class BossFactory {

    private BossFactory() {}

    public static @NotNull PreparedBoss createBoss(
            @NotNull Match match,
            @NotNull BossModel bossModel
    ) throws ValueParseException {
        if (!bossModel.isAllBasicFieldsCompleted()) {
            throw new ValueParseException("Incomplete BossModel");
        }

        BossType type = BossType.REGISTRY.get(bossModel.typeName());

        if (type == null)
            throw new ValueParseException("Unknown boss type: " + bossModel.typeName());

        EntityType entityType = type.entityType();

        if (entityType == null) {
            entityType = bossModel.entityType();
        }

        if (entityType == null) {
            throw new ValueParseException("No EntityType specified for boss type: " + bossModel.typeName());
        }

        EntityType finalEntityType = entityType;
        return new PreparedBoss(bossModel, () -> {
            Location location = bossModel.spawnLocation().toLocationBySchemeName(
                    match.runningMap().worlds()
            );

            World world = location.getWorld();
            LivingEntity livingEntity = (LivingEntity) world.spawnEntity(location, finalEntityType);

            livingEntity.setCustomName(bossModel.displayName());
            livingEntity.setCustomNameVisible(true);
            livingEntity.setMaxHealth(bossModel.health());
            livingEntity.setHealth(bossModel.health());
            return livingEntity;
        });
    }
}
