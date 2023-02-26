package com.github.ynverxe.dtn.tickable;

import com.github.ynverxe.dtn.model.instance.Named;

public interface Tickable extends Named {
    void tick();
}
