package com.github.ynverxe.dtn.effect;

import org.bukkit.Effect;
import org.jetbrains.annotations.NotNull;

public class BukkitEffect {

    private final Effect effect;
    private final Object data;

    public BukkitEffect(@NotNull Effect effect, Object data) {
        this.effect = effect;
        this.data = data;
    }

    public @NotNull Effect effect() {
        return effect;
    }

    public @NotNull Object data() {
        return data;
    }
}
