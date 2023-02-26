package com.github.ynverxe.dtn.model.data;

import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleModel {

    private final @NotNull Particle particle;
    private final int amount;
    private final float offX;
    private final float offY;
    private final float offZ;
    private final float extra;
    private final @Nullable Object data;

    public ParticleModel(@NotNull Particle particle, int amount, float offX, float offY, float offZ, float extra, @Nullable Object data) {
        this.particle = particle;
        this.amount = amount;
        this.offX = offX;
        this.offY = offY;
        this.offZ = offZ;
        this.extra = extra;
        this.data = data;
    }

    public ParticleModel(Particle particle, float offX, float offY, float offZ, Object data) {
        this(particle, 1, offX, offY, offZ, 0.0f, data);
    }

    public ParticleModel(Particle particle, int amount, float offX, float offY, float offZ, float extra) {
        this(particle, amount, offX, offY, offZ, extra, null);
    }

    public ParticleModel(Particle particle, int offSetX, int offSetY, int offSetZ) {
        this(particle, (float) offSetX, (float) offSetY, (float) offSetZ, null);
    }

    public ParticleModel(Particle particle, int amount) {
        this(particle, amount, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Particle particle() {
        return particle;
    }

    public float offX() {
        return offX;
    }

    public float offY() {
        return offY;
    }

    public float offZ() {
        return offZ;
    }

    public float extra() {
        return extra;
    }

    public int amount() {
        return amount;
    }

    public @Nullable Object data() {
        return data;
    }

}
