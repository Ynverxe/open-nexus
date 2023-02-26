package com.github.ynverxe.dtn.factory;

import com.github.ynverxe.dtn.dimension.edition.EditionHandler;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import org.jetbrains.annotations.NotNull;

public interface EditionHandlerFactory {
    @NotNull EditionHandler createEditionHandler(@NotNull EditionInstance p0);
}
