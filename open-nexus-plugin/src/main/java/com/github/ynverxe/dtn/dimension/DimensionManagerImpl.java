package com.github.ynverxe.dtn.dimension;

import java.util.Collection;
import java.util.Set;
import java.util.Optional;

import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.operation.dimension.CreateDimensionOperation;
import com.github.ynverxe.dtn.operation.dimension.LoadDimensionOperation;
import com.github.ynverxe.dtn.operation.dimension.SaveDimensionOperation;
import com.github.ynverxe.dtn.world.WorldContainer;
import com.github.ynverxe.dtn.world.WorldContainerImpl;
import com.github.ynverxe.dtn.world.WorldHelper;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.util.ExceptionHandler;
import com.github.ynverxe.dtn.environment.DTNEnvironment;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.database.DirectoryDatabaseHandler;
import com.github.ynverxe.dtn.storage.GsonMapper;
import com.github.ynverxe.dtn.gson.GlobalGson;
import java.util.concurrent.ConcurrentHashMap;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;
import java.util.Map;

public class DimensionManagerImpl implements DimensionManager {

    private final Map<String, Dimension> dimensionMap;
    private final PlainTextModelRepository<DimensionModel> dimensionModelRepository;
    private final DimensionFactory dimensionFactory;
    
    public DimensionManagerImpl() {
        this.dimensionMap = new ConcurrentHashMap<>();

        this.dimensionModelRepository = PlainTextModelRepository.createRepository(
                new GsonMapper<>(GlobalGson.BASE, DimensionModel.class),
                DirectoryDatabaseHandler.onPluginFolder("dimensions", ".json")
        );

        this.dimensionFactory = new DimensionFactoryImpl();
    }

    @Override
    public @NotNull Dimension createEmptyDimension(@NotNull String name, boolean saveWorldInFile, @NotNull String typeName ,@NotNull PropertiesContainer container) {
        WorldContainer worldContainer = new WorldContainerImpl();
        World world = WorldHelper.instance().createEmptyWorld(name, saveWorldInFile);

        worldContainer.addWorld(world);
        Dimension dimension = new DimensionImpl(this, name, worldContainer, typeName, container, new Date());
        appendDimension(dimension);

        return dimension;
    }

    @Override
    public @NotNull List<Dimension> createDimensions(@NotNull DimensionModel.Named... dimensionModels) {
        List<Dimension> dimensions = new ArrayList<>();

        for (DimensionModel.Named dimensionModel : dimensionModels) {
            CreateDimensionOperation operation = new CreateDimensionOperation(dimensionModel, dimensionFactory, this::appendDimension);
            Dimension dimension = operation.execute();

            if (dimension == null) continue;

            dimensions.add(dimension);
        }

        return dimensions;
    }
    
    public void removeDimension(@NotNull Object... dimensions) {
        for (Object dimension : dimensions) {
            if (dimension instanceof String) {
                dimensionMap.remove(dimension);
            } else {
                if (!(dimension instanceof Dimension)) {
                    throw new IllegalArgumentException("Invalid argument '" + dimension + "'");
                }
                dimensionMap.remove(((Dimension)dimension).name());
            }
        }
    }
    
    public @Nullable Dimension loadDimension(@NotNull String key, boolean cache) {
        LoadDimensionOperation operation = new LoadDimensionOperation(this, dimensionModelRepository, key, cache);

        return operation.execute();
    }
    
    @NotNull
    public CompletableFuture<Dimension> loadDimensionAsync(@NotNull String key, boolean cache) {
        LoadDimensionOperation operation = new LoadDimensionOperation(this, dimensionModelRepository, key, cache);

        return CompletableFuture.supplyAsync(operation, DTNEnvironment.instance().STORAGE_EXECUTOR);
    }
    
    public boolean saveDimension(@NotNull String dimensionName) {
        Dimension dimension = dimensionMap.get(dimensionName);
        if (dimension == null) {
            return false;
        }

        SaveDimensionOperation saveDimensionOperation = new SaveDimensionOperation(dimension, dimensionModelRepository);

        return saveDimensionOperation.execute() != null;
    }
    
    public CompletableFuture<Boolean> saveDimensionAsync(@NotNull String dimensionName) {
        return CompletableFuture.supplyAsync(() -> saveDimension(dimensionName), DTNEnvironment.instance().STORAGE_EXECUTOR)
                .exceptionally(new ExceptionHandler<>());
    }
    
    @NotNull
    public DimensionFactory dimensionFactory() {
        return dimensionFactory;
    }
    
    @Nullable
    public Dimension get(@NotNull String key) {
        return dimensionMap.get(key);
    }
    
    @NotNull
    public Optional<Dimension> safeGet(@NotNull String key) {
        return Optional.ofNullable(get(key));
    }
    
    @NotNull
    public Set<String> keys() {
        return Collections.unmodifiableSet(dimensionMap.keySet());
    }
    
    @NotNull
    public Collection<Dimension> values() {
        return Collections.unmodifiableCollection(dimensionMap.values());
    }
    
    @NotNull
    public Set<Map.Entry<String, Dimension>> entries() {
        return Collections.unmodifiableSet(dimensionMap.entrySet());
    }
    
    public boolean has(@NotNull String key) {
        return dimensionMap.containsKey(key);
    }
    
    public int cachedSize() {
        return dimensionMap.size();
    }
    
    @NotNull
    public PlainTextModelRepository<DimensionModel> dimensionDataRepository() {
        return dimensionModelRepository;
    }
    
    @NotNull
    public CompletableFuture<Void> saveAll(boolean async) {
        Runnable runnable = () -> keys().forEach(this::saveDimension);

        if (async) {
            return CompletableFuture.runAsync(runnable, DTNEnvironment.instance().STORAGE_EXECUTOR);
        }

        runnable.run();
        return CompletableFuture.completedFuture(null);
    }
    
    private void appendDimension(Dimension dimension) {
        dimensionMap.put(dimension.name(), dimension);
    }
}
