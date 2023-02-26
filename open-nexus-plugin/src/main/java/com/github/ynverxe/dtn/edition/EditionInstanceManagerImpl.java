package com.github.ynverxe.dtn.edition;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import com.github.ynverxe.dtn.dimension.Dimension;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.edition.defaults.MatchMapEditionHandler;
import java.util.HashMap;
import com.github.ynverxe.dtn.factory.EditionHandlerFactory;
import java.util.Map;
import com.github.ynverxe.dtn.dimension.edition.EditionInstanceManager;

public class EditionInstanceManagerImpl implements EditionInstanceManager {
    private final Map<String, EditionInstanceImpl> instances;
    private final Map<String, EditionHandlerFactory> configurationHandlers;
    
    public EditionInstanceManagerImpl() {
        this.instances = new HashMap<>();
        this.configurationHandlers = new HashMap<>();
        this.addHandlerFactory("match-map", MatchMapEditionHandler::new);
    }
    
    @NotNull
    public EditionInstance configureDimension(@NotNull APlayer agent, @NotNull Dimension dimension) throws IllegalArgumentException {
        if (this.findByAgent(agent).isPresent()) {
            throw new IllegalArgumentException(agent.name() + " is already configuring a dimension");
        }

        EditionInstanceImpl instance = this.instances.get(dimension.name());
        if (instance == null) {
            String typeName = dimension.typeName();
            EditionHandlerFactory handlerFactory = this.configurationHandlers.get(dimension.typeName());
            if (handlerFactory == null) {
                throw new IllegalArgumentException("no handler factory found for dimension type name '" + typeName + "'");
            }

            instance = new EditionInstanceImpl(dimension, handlerFactory, this);
            this.instances.put(dimension.name(), instance);
        }

        instance.appendAgent(agent);
        return instance;
    }
    
    @NotNull
    public EditionInstance configureDimension(@NotNull APlayer agent, @NotNull String dimensionName) throws IllegalArgumentException {
        DimensionManager dimensionManager = DestroyTheNexus.instance().dimensionManager();
        IllegalArgumentException ex;
        Dimension dimension = dimensionManager.safeGet(dimensionName).orElseThrow(() ->
                new IllegalArgumentException("no dimension found with name '" + dimensionName + "'"));

        return this.configureDimension(agent, dimension);
    }
    
    @NotNull
    public Optional<EditionInstance> findByAgent(@NotNull APlayer agent) {
        List<EditionInstance> instanceList = this.instances.values()
                .stream()
                .filter(instance -> instance.agents().contains(agent)).collect(Collectors.toList());

        if (instanceList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(instanceList.get(0));
    }
    
    @NotNull
    public EditionInstanceManager addHandlerFactory(@NotNull String dimensionTypeName, @NotNull EditionHandlerFactory handlerFactory) {
        this.configurationHandlers.put(
                Objects.requireNonNull(dimensionTypeName, "dimensionTypeName"),
                Objects.requireNonNull(handlerFactory, "handlerFactory")
        );

        return this;
    }
    
    @Nullable
    public EditionInstance get(@NotNull String key) {
        return this.instances.get(key);
    }
    
    @NotNull
    public Optional<EditionInstance> safeGet(@NotNull String key) {
        return Optional.ofNullable(this.get(key));
    }
    
    @NotNull
    public Set<String> keys() {
        return Collections.unmodifiableSet(this.instances.keySet());
    }
    
    @NotNull
    public Collection<EditionInstance> values() {
        return Collections.unmodifiableCollection(this.instances.values());
    }
    
    @NotNull
    public Set<Map.Entry<String, EditionInstance>> entries() {
        return Collections.unmodifiableSet(this.instances.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (EditionInstance) entry.getValue()))
                .collect(Collectors.toSet())
        );
    }
    
    public boolean has(@NotNull String key) {
        return this.instances.containsKey(key);
    }
    
    public int cachedSize() {
        return this.instances.size();
    }
    
    void handleInstanceFinalization(EditionInstanceImpl configurationInstance) {
        final String key = configurationInstance.dimension().name();
        if (this.instances.remove(key) == null) {
            throw new IllegalArgumentException("no dimension stored with key '" + key + "'");
        }
        for (APlayer agent : configurationInstance.agents()) {
            DestroyTheNexus.instance().sendToLobby(agent);
        }
    }
}
