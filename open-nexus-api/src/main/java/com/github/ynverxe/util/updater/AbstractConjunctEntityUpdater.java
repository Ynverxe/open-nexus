package com.github.ynverxe.util.updater;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractConjunctEntityUpdater<T> implements ConjunctEntityUpdater {

    protected final Logger logger;
    protected final List<Object> problematicEntities;

    public AbstractConjunctEntityUpdater(Logger logger) {
        problematicEntities = new CopyOnWriteArrayList<>();
        this.logger = logger;
    }

    @Override
    public void run() {
        for (T entity : entities()) {
            final Object entityId = resolveEntityId(entity);
            if (problematicEntities.contains(entityId)) {
                continue;
            }
            try {
                consumeEntity(entity);
            } catch (Exception exception) {
                problematicEntities.add(entityId);
                logger.log(Level.SEVERE, problematicEntityDebugMessage(entity), entityId);
                logger.log(Level.SEVERE, exception, entityId::toString);
            }
        }
    }

    @NotNull protected abstract String updaterId();

    protected abstract void consumeEntity(@NotNull T p0);

    @NotNull protected abstract Collection<T> entities();

    @NotNull protected Object resolveEntityId(@NotNull T entity) {
        return entity;
    }

    @NotNull protected abstract String problematicEntityDebugMessage(T p0);

    @Override
    public void clearProblematicEntities() {
        problematicEntities.clear();
        logger.log(Level.INFO, "{0} problematic entities cleared", updaterId());
    }
}
