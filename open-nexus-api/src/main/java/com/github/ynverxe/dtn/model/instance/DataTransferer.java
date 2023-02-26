package com.github.ynverxe.dtn.model.instance;

public interface DataTransferer<T> {

    void shareData(T recipient);

}