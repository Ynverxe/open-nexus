package com.github.ynverxe.dtn.dimension;

import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DimensionModel {

    private final List<String> worldSchemes;
    private final String typeName;
    private final PropertiesContainer properties;
    private final Date creationDate;

    public DimensionModel(List<String> worldSchemes, String typeName, PropertiesContainer properties, Date creationDate) {
        this.worldSchemes = worldSchemes;
        this.typeName = typeName;
        this.properties = properties;
        this.creationDate = creationDate;
    }

    public static DimensionModel fromDimension(Dimension dimension) {
        return new DimensionModel(
                dimension.worldContainer()
                        .worlds()
                        .stream()
                        .map(World::getName)
                        .collect(Collectors.toList()),
                dimension.typeName(),
                dimension.properties().copy(),
                dimension.creationDate()
        );
    }

    public Date creationDate() {
        return creationDate;
    }

    public Named withName(@NotNull String name) {
        return new Named(name, new ArrayList<>(worldSchemes), typeName, properties, creationDate);
    }

    public List<String> worldSchemes() {
        return worldSchemes;
    }

    public String typeName() {
        return typeName;
    }

    public PropertiesContainer properties() {
        return properties;
    }

    public static final class Named extends DimensionModel {

        private final String name;

        public Named(String name, List<String> worldSchemes, String typeName, PropertiesContainer properties, Date creationDate) {
            super(worldSchemes, typeName, properties, creationDate);
            this.name = name;
        }

        public String name() {
            return name;
        }
    }
}
