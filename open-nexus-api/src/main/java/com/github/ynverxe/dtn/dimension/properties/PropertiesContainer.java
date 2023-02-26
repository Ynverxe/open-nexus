package com.github.ynverxe.dtn.dimension.properties;

import com.github.ynverxe.dtn.model.instance.DataTransferer;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.NotNull;

public interface PropertiesContainer extends ImplicitlyDeserializable, ModelDataTree, DataTransferer<PropertiesContainer> {

    @NotNull String typeName();

    @NotNull PropertiesContainer copy();

    @Override
    default void shareData(PropertiesContainer recipient) {
        forEach(recipient::setValue);
    }

    default <T> T expectImplicitlyDeserializeValue(String path) {
        return ImplicitlyDeserializable.expectModel(getValue(path));
    }

    @Override
    default String alias() {
        return typeName();
    }

    @Override
    default ModelDataValue toModelData() {
        return new ModelDataValue(this);
    }
}