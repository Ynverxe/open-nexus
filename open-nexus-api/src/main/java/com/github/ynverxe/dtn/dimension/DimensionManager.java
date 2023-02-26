package com.github.ynverxe.dtn.dimension;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.exception.NoDimensionFoundException;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;
import com.github.ynverxe.util.cache.CacheModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DimensionManager extends CacheModel<String, Dimension> {

    @NotNull Dimension createEmptyDimension(@NotNull String name, boolean saveWorldInFile, @NotNull String typeName, @NotNull PropertiesContainer propertiesContainer);

    List<Dimension> createDimensions(@NotNull DimensionModel.Named... p0);

    void removeDimension(@NotNull Object... p0);

    @Nullable Dimension loadDimension(@NotNull String p0, boolean p1) throws NoDimensionFoundException;

    @NotNull CompletableFuture<Dimension> loadDimensionAsync(@NotNull String p0, boolean p1);

    boolean saveDimension(@NotNull String p0);

    CompletableFuture<Boolean> saveDimensionAsync(@NotNull String p0);

    @NotNull DimensionFactory dimensionFactory();

    @NotNull PlainTextModelRepository<DimensionModel> dimensionDataRepository();

    @NotNull CompletableFuture<Void> saveAll(boolean p0);

    static @NotNull DimensionManager instance() {
        return DestroyTheNexus.instance().dimensionManager();
    }

}
