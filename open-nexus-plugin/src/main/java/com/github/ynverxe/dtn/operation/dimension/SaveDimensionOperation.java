package com.github.ynverxe.dtn.operation.dimension;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.DimensionModel;
import com.github.ynverxe.dtn.operation.AbstractOperation;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveDimensionOperation extends AbstractOperation<Object> {

    private final Logger logger = DestroyTheNexus.LOGGER;
    private final Dimension dimension;
    private final PlainTextModelRepository<DimensionModel> dimensionModelRepository;

    public SaveDimensionOperation(Dimension dimension, PlainTextModelRepository<DimensionModel> dimensionModelRepository) {
        this.dimension = dimension;
        this.dimensionModelRepository = dimensionModelRepository;
    }

    @Override
    protected Object run() throws Throwable {
        DimensionModel dimensionModel = DimensionModel.fromDimension(dimension);
        dimensionModelRepository.save(dimension.name(), dimensionModel);

        logger.log(
                Level.INFO,
                "&aDimension[%s, %s, %s] saved!",
                new Object[] { dimension.name(), dimension.typeName(), dimensionModel.worldSchemes() }
        );

        return new Object();
    }
}