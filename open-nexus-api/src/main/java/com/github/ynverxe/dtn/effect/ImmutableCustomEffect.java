package com.github.ynverxe.dtn.effect;

import com.github.ynverxe.dtn.model.data.ParticleModel;
import com.github.ynverxe.dtn.model.data.SoundModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImmutableCustomEffect extends CustomEffect {
    ImmutableCustomEffect(List<Object> values) {
        super(values);
    }

    @NotNull @Override
    public CustomEffect addBukkitEffect(@NotNull BukkitEffect bukkitEffect) {
        throw new UnsupportedOperationException("immutable effect");
    }

    @NotNull @Override
    public CustomEffect addParticle(@NotNull ParticleModel particleModel) {
        throw new UnsupportedOperationException("immutable effect");
    }

    @NotNull @Override
    public CustomEffect addSound(@NotNull SoundModel soundModel) {
        throw new UnsupportedOperationException("immutable effect");
    }
}
