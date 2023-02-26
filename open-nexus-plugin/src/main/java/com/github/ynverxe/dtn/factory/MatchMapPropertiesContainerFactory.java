package com.github.ynverxe.dtn.factory;

import java.util.Collections;
import java.util.List;

import com.github.ynverxe.dtn.data.ModelDataParser;
import com.github.ynverxe.dtn.dimension.properties.MatchMapPropertiesContainer;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import org.jetbrains.annotations.NotNull;

public class MatchMapPropertiesContainerFactory implements PropertiesContainerFactory<MatchMapPropertiesContainer> {

    public MatchMapPropertiesContainerFactory() {
        ImplicitlyDeserializable.PARSERS.put("match-map", (ModelDataParser<MatchMapPropertiesContainer>) value -> {
            MatchMapPropertiesContainer container = new MatchMapPropertiesContainer();
            container.consume(value.asTree(), true);
            return container;
        });
    }

    @NotNull
    public String name() {
        return "match-map";
    }
    
    @NotNull
    public Class<MatchMapPropertiesContainer> productType() {
        return MatchMapPropertiesContainer.class;
    }
    
    @NotNull
    public MatchMapPropertiesContainer createNewProperties() {
        return new MatchMapPropertiesContainer();
    }
    
    @NotNull
    public List<String> description() {
        return Collections.singletonList("Properties for DTN match maps");
    }
}
