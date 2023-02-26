package com.github.ynverxe.dtn.data;

import com.github.ynverxe.structured.data.ModelDataHolder;
import com.github.ynverxe.structured.data.ModelDataValue;

public interface BackedModel extends ModelDataHolder {

    ModelDataValue getRawData();

    @Override
    default ModelDataValue toModelData() {
        return getRawData();
    }
}