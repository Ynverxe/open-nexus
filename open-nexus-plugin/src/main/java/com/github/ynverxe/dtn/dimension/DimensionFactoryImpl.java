package com.github.ynverxe.dtn.dimension;

import com.github.ynverxe.dtn.world.WorldContainer;
import com.github.ynverxe.dtn.world.WorldContainerImpl;
import com.github.ynverxe.dtn.world.WorldHelper;
import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class DimensionFactoryImpl implements DimensionFactory {

    DimensionFactoryImpl() {
    }
    
    @NotNull
    public Dimension createDimension(@NotNull DimensionModel.Named dimensionModel) {
        WorldContainer worldContainer = new WorldContainerImpl();

        dimensionModel.worldSchemes()
                .stream()
                .map(worldName -> WorldHelper.instance().createWorldIfAbsent(worldName, true))
                .forEach(worldContainer::addWorld);

        Date creationDate = dimensionModel.creationDate();

        return new DimensionImpl(
                DimensionManager.instance(),
                dimensionModel.name(),
                worldContainer,
                dimensionModel.typeName(),
                dimensionModel.properties().copy(),
                (creationDate != null) ? creationDate : new Date()
        );
    }
}
