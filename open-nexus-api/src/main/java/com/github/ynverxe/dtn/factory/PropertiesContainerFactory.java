package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.dimension.properties.PropertiesContainer;
import com.github.ynverxe.dtn.registry.ObjectRegistry;
import com.github.ynverxe.dtn.registry.Registrable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PropertiesContainerFactory<T extends PropertiesContainer> extends Registrable {

    ObjectRegistry<PropertiesContainerFactory<?>> REGISTRY = ObjectRegistry.create("PropertiesContainerFactoryRegistry");

    @NotNull String name();

    @NotNull Class<T> productType();

    @NotNull T createNewProperties();

    @NotNull List<String> description();

    @Override
    default String id() {
        return name();
    }

}
