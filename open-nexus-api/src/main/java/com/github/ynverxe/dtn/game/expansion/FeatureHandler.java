package com.github.ynverxe.dtn.game.expansion;

import com.github.ynverxe.dtn.authorization.Authorization;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.jetbrains.annotations.NotNull;

public interface FeatureHandler {
    Authorization AUTHORIZED = new Authorization(ResourceReference.simple("all in order!"), true);

    default @NotNull Authorization authorizeToStart(@NotNull GameInstance gameInstance, @NotNull Dimension selectedDimension) {
        return FeatureHandler.AUTHORIZED;
    }
}
