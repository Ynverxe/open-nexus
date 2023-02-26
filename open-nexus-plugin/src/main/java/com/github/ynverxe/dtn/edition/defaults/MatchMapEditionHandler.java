package com.github.ynverxe.dtn.edition.defaults;

import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.dimension.properties.MatchMapPropertiesContainer;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import com.github.ynverxe.dtn.player.APlayer;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.edition.EditionHandler;

public class MatchMapEditionHandler implements EditionHandler {
    private final MatchMapPropertiesContainer container;
    private final Dimension dimension;
    
    public MatchMapEditionHandler(EditionInstance editionInstance) {
        this.container = (MatchMapPropertiesContainer) editionInstance.dimension().properties().copy();
        this.dimension = editionInstance.dimension();
    }
    
    @NotNull
    public MatchMapPropertiesContainer container() {
        return this.container;
    }
    
    public void prepareAgent(@NotNull APlayer player) {
        Player bPlayer = player.bukkitPlayer();
        bPlayer.setGameMode(GameMode.CREATIVE);
        bPlayer.getInventory().clear();
    }
    
    public void save() {
        MatchMapPropertiesContainer original = this.dimension.properties();
        container.shareData(original);
        DimensionManager.instance().saveDimension(dimension.name());
    }
}