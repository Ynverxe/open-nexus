package com.github.ynverxe.dtn.operation.dimension;

import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.dimension.DimensionModel;
import com.github.ynverxe.dtn.exception.NoDimensionFoundException;
import com.github.ynverxe.dtn.operation.AbstractOperation;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;

import java.util.List;

public class LoadDimensionOperation extends AbstractOperation<Dimension> {

    private final DimensionManager dimensionManager;
    private final PlainTextModelRepository<DimensionModel> dimensionModelRepository;
    private final String key;
    private final boolean cache;

    public LoadDimensionOperation(
            DimensionManager dimensionManager,
            PlainTextModelRepository<DimensionModel> dimensionModelRepository,
            String key,
            boolean cache
    ) {
        this.dimensionManager = dimensionManager;
        this.dimensionModelRepository = dimensionModelRepository;
        this.key = key;
        this.cache = cache;

        setFailMessage("Unable to load dimension");
    }

    @Override
    protected Dimension run() throws Throwable {
        DimensionModel dimensionModel = dimensionModelRepository.find(key).orElseThrow(() -> new NoDimensionFoundException(key));
        dimensionManager.createDimensions(dimensionModel.withName(key));

        return dimensionManager.get(key);
    }
}