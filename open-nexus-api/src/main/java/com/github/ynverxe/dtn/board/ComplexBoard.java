package com.github.ynverxe.dtn.board;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ComplexBoard extends DTNBoard, Runnable {
    void setTitleComponent(@NotNull BoardComponent p0);

    @NotNull ComplexBoard setLineComponent(int p0, @NotNull BoardComponent p1);

    @NotNull ComplexBoard setLineComponents(@NotNull List<BoardComponent> p0);
}
