package com.github.ynverxe.dtn.dimension;

import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.world.WorldContainer;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Date;
import com.github.ynverxe.dtn.model.instance.AbstractTerminable;

@SuppressWarnings("unchecked")
public final class DimensionImpl extends AbstractTerminable implements Dimension {

    private final DimensionManager dimensionManager;
    private final String name;
    private final String typeName;
    private final PropertiesContainer properties;
    private final Date creationDate;
    private WorldContainer worldContainer;
    
    public DimensionImpl(
            DimensionManager dimensionManager,
            String name,
            WorldContainer worldContainer,
            String typeName,
            PropertiesContainer properties,
            Date creationDate
    ) {
        this.dimensionManager = dimensionManager;
        this.name = Objects.requireNonNull(name, "name");
        this.worldContainer = worldContainer;
        this.typeName = Objects.requireNonNull(typeName, "typeName");
        this.properties = Objects.requireNonNull(properties, "properties");
        this.creationDate = Objects.requireNonNull(creationDate, "creationDate");
    }
    
    @NotNull
    public String name() {
        return this.name;
    }

    @Override
    public @NotNull WorldContainer worldContainer() {
        return worldContainer;
    }

    @NotNull
    public <T extends PropertiesContainer> T properties() {
        this.checkNotTerminated();
        return (T)this.properties;
    }
    
    @NotNull
    public String typeName() {
        return this.typeName;
    }
    
    @NotNull
    public Date creationDate() {
        return this.creationDate;
    }
    
    public void checkIsOfType(@NotNull String expectedType) {
        if (!this.typeName.equals(expectedType)) {
            throw new IllegalArgumentException("Dimension is not of the type: " + expectedType + ", but: " + this.typeName);
        }
    }
    
    protected void preTermination() {
        this.dimensionManager.removeDimension(this);
        this.worldContainer.worlds().clear();
        this.worldContainer = null;
    }
}
