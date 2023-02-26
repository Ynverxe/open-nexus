package com.github.ynverxe.dtn.edition;

import com.github.ynverxe.dtn.model.instance.AbstractTerminable;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.DestroyTheNexus;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import com.github.ynverxe.dtn.factory.EditionHandlerFactory;
import com.github.ynverxe.dtn.dimension.edition.EditionHandler;
import com.github.ynverxe.dtn.player.APlayer;
import java.util.List;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;

public class EditionInstanceImpl extends AbstractTerminable implements EditionInstance {

    private final Dimension dimension;
    private final List<APlayer> agents;
    private final EditionHandler editionHandler;
    private final EditionInstanceManagerImpl configurationInstanceManager;
    
    public EditionInstanceImpl(Dimension dimension, EditionHandlerFactory editionHandlerFactory, EditionInstanceManagerImpl configurationInstanceManager) {
        this.agents = new ArrayList<>();
        this.dimension = dimension;
        this.configurationInstanceManager = configurationInstanceManager;
        this.editionHandler = editionHandlerFactory.createEditionHandler(this);
    }
    
    @NotNull
    public Dimension dimension() {
        return this.dimension;
    }
    
    @NotNull
    public List<APlayer> agents() {
        return this.agents;
    }
    
    @NotNull
    public EditionHandler configurationHandler() {
        return this.editionHandler;
    }
    
    public void save(boolean saveWorlds, boolean saveData) {
        this.editionHandler.save();
        if (saveWorlds) {
            this.dimension.worldContainer().forEach(World::save);
        }

        DestroyTheNexus.instance().dimensionManager().saveDimensionAsync(dimension.name());
    }
    
    public void appendAgent(APlayer agent) {
        agents.add(agent);
        editionHandler.prepareAgent(agent);
        agent.teleport(dimension.worldContainer().getWorldByIndex(0));
        agent.renderResource(DefaultTranslationContainer.CONFIGURE_DIMENSION.replacing("<dimension>", this.dimension.name()));
    }
    
    protected void preTermination() {
        this.configurationInstanceManager.handleInstanceFinalization(this);
    }
}
