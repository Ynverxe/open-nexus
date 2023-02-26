package com.github.ynverxe.dtn.game;

import java.util.stream.Collectors;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.util.updater.AbstractConjunctEntityUpdater;

public class GameInstanceUpdaterImpl extends AbstractConjunctEntityUpdater<GameInstanceImpl> implements GameInstanceUpdater {
    public GameInstanceUpdaterImpl() {
        super(DestroyTheNexus.LOGGER);
    }
    
    public void consumeEntity(@NotNull GameInstanceImpl gameInstance) {
        gameInstance.update();
    }
    
    @NotNull
    protected String updaterId() {
        return "GameInstanceUpdater";
    }
    
    @NotNull
    protected Collection<GameInstanceImpl> entities() {
        return GameManager.instance().activeInstances().stream()
                .map(gameInstance -> (GameInstanceImpl) gameInstance)
                .collect(Collectors.toList());
    }
    
    @NotNull
    protected Object resolveEntityId(@NotNull GameInstanceImpl entity) {
        return entity.id();
    }
    
    @NotNull
    protected String problematicEntityDebugMessage(GameInstanceImpl entity) {
        return "Game instance of the room '" + entity.name() + "' with id {0} caused an exception, adding it to the black list...";
    }
}
