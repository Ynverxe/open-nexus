package com.github.ynverxe.dtn.dimension.properties;

import com.github.ynverxe.structured.data.ModelDataTreeImpl;
import org.jetbrains.annotations.NotNull;

public final class RoomLobbyPropertiesContainer extends ModelDataTreeImpl implements PropertiesContainer {

    @Override
    public @NotNull PropertiesContainer copy() {
        return new RoomLobbyPropertiesContainer();
    }

    @Override
    public @NotNull String typeName() {
        return "room-lobby";
    }

    @Override
    public @NotNull PropertiesContainer clone() {
        try {
            return (PropertiesContainer) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
