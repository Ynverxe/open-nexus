package com.github.ynverxe.dtn.tickable;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.model.instance.Terminable;
import com.github.ynverxe.util.updater.AbstractConjunctEntityUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TickableRegistry extends AbstractConjunctEntityUpdater<Tickable> {
    private final List<Tickable> registeredTickables;

    public TickableRegistry() {
        super(DestroyTheNexus.LOGGER);
        registeredTickables = Collections.synchronizedList(new ArrayList<>());
    }

    public @NotNull static TickableRegistry instance() {
        return DestroyTheNexus.instance().tickableRegistry();
    }

    public void registerNewTickableEntity(@NotNull Tickable tickableEntity) {
        for (Tickable registeredTickable : registeredTickables) {
            final String newEntityId = tickableEntity.name();
            final String currentEntityId = registeredTickable.name();
            if (newEntityId.equals(currentEntityId)) {
                throw new IllegalArgumentException(currentEntityId + " is already registered by another entity.");
            }
        }
        registeredTickables.add(tickableEntity);
    }

    public boolean unregisterTickable(@NotNull Tickable tickable) {
        return registeredTickables.remove(tickable);
    }

    @NotNull @Override
    protected String updaterId() {
        return "TickableEntitiesUpdater";
    }

    @Override
    protected void consumeEntity(@NotNull Tickable entity) {
        if (!(entity instanceof Terminable) || !((Terminable) entity).terminated()) {
            entity.tick();
        }
    }

    @NotNull @Override
    protected Collection<Tickable> entities() {
        return registeredTickables;
    }

    @NotNull @Override
    protected String problematicEntityDebugMessage(Tickable entity) {
        return "Tickable Entity '" + entity.name() + "' caused an exception, stop updating it...";
    }
}
