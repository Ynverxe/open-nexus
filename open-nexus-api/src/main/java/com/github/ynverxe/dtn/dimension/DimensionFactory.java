package com.github.ynverxe.dtn.dimension;

import org.jetbrains.annotations.NotNull;

public interface DimensionFactory {
    @NotNull Dimension createDimension(@NotNull DimensionModel.Named dimensionModel);
}