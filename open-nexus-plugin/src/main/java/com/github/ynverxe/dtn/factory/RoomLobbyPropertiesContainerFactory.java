package com.github.ynverxe.dtn.factory;

import java.util.Collections;
import java.util.List;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.dtn.dimension.properties.RoomLobbyPropertiesContainer;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import org.jetbrains.annotations.NotNull;

public class RoomLobbyPropertiesContainerFactory implements PropertiesContainerFactory<RoomLobbyPropertiesContainer> {

    public RoomLobbyPropertiesContainerFactory() {
        ImplicitlyDeserializable.PARSERS.put("room-lobby", (ModelDataParser<RoomLobbyPropertiesContainer>) value -> {
            RoomLobbyPropertiesContainer container = new RoomLobbyPropertiesContainer();
            container.consume(value.asTree(), true);
            return container;
        });
    }

    @NotNull
    public String name() {
        return "room-lobby";
    }
    
    @NotNull
    public Class<RoomLobbyPropertiesContainer> productType() {
        return RoomLobbyPropertiesContainer.class;
    }
    
    @NotNull
    public RoomLobbyPropertiesContainer createNewProperties() {
        return new RoomLobbyPropertiesContainer();
    }
    
    @NotNull
    public List<String> description() {
        return Collections.singletonList("Properties type for DTN lobbies.");
    }
}