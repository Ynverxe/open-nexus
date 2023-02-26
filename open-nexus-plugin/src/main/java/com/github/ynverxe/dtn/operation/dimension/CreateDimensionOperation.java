package com.github.ynverxe.dtn.operation.dimension;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.DimensionFactory;
import com.github.ynverxe.dtn.dimension.DimensionModel;
import com.github.ynverxe.dtn.operation.AbstractOperation;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateDimensionOperation extends AbstractOperation<Dimension> {

    private final Logger logger = DestroyTheNexus.LOGGER;
    private final DimensionModel.Named dimensionModel;
    private final DimensionFactory dimensionFactory;
    private final Consumer<Dimension> dimensionAppender;

    public CreateDimensionOperation(
            DimensionModel.Named dimensionModel,
            DimensionFactory dimensionFactory,
            Consumer<Dimension> dimensionAppender
    ) {
        this.dimensionModel = dimensionModel;
        this.dimensionFactory = dimensionFactory;
        this.dimensionAppender = dimensionAppender;

        setFailMessage("Unable to load dimension '" + dimensionModel.name() + "'");
    }

    @Override
    protected Dimension run() throws Throwable {
        Dimension dimension = dimensionFactory.createDimension(dimensionModel);

        dimensionAppender.accept(dimension);
        logger.log(
                Level.INFO,
                "&aDimension[{0}, {1}, {2}] loaded!",
                new Object[] { dimension.name(), dimension.typeName(), dimensionModel.worldSchemes() }
        );

        return dimension;
    }
}