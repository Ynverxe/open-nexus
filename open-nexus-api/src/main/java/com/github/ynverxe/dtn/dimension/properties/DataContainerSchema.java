package com.github.ynverxe.dtn.dimension.properties;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface DataContainerSchema {

    void validateSchemaCompliance();

    @NotNull Map<String, Object> interpretAll(boolean excludeMissingValues);

}