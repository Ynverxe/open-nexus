package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.kit.Equipment;
import com.github.ynverxe.dtn.kit.Kit;
import com.github.ynverxe.dtn.player.MatchPlayer;
import org.jetbrains.annotations.NotNull;

public interface EquipmentFactory {
    @NotNull Equipment createEquipment(@NotNull Kit p0, @NotNull MatchPlayer p1);
}
