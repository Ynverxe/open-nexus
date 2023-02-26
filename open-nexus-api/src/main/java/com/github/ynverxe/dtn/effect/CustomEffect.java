package com.github.ynverxe.dtn.effect;

import com.github.ynverxe.dtn.model.data.ParticleModel;
import com.github.ynverxe.dtn.model.data.SoundModel;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomEffect implements Cloneable {

    private final List<Object> values;

    protected CustomEffect(List<Object> values) {
        this.values = new ArrayList<>(values);
    }

    public CustomEffect() {
        this(Collections.emptyList());
    }

    public CustomEffect addParticle(@NotNull ParticleModel particleModel) {
        values.add(particleModel);
        return this;
    }

    public CustomEffect addParticle(@NotNull Particle particle, int amount, int extra, @Nullable Object data) {
        return addParticle(new ParticleModel(particle, amount, 0.0f, 0.0f, 0.0f, (float) extra, data));
    }

    public CustomEffect addSound(@NotNull SoundModel soundModel) {
        values.add(soundModel);
        return this;
    }

    public CustomEffect addSound(@NotNull Sound sound, float volume, float pitch) {
        return addSound(new SoundModel(sound, volume, pitch));
    }

    public CustomEffect addBukkitEffect(@NotNull BukkitEffect bukkitEffect) {
        values.add(bukkitEffect);
        return this;
    }

    public CustomEffect addBukkitEffect(@NotNull Effect effect, @Nullable final Object data) {
        return addBukkitEffect(new BukkitEffect(effect, data));
    }

    public void playAt(@NotNull Location location) {
        World world = location.getWorld();
        for (Object value : values) {
            if (value instanceof BukkitEffect) {
                BukkitEffect bukkitEffect = (BukkitEffect) value;
                world.playEffect(location, bukkitEffect.effect(), bukkitEffect.data());
            } else if (value instanceof ParticleModel) {
                ParticleModel particleModel = (ParticleModel) value;
                world.spawnParticle(particleModel.particle(), location, particleModel.amount(), particleModel.offX(), particleModel.offY(), particleModel.offZ(), particleModel.extra(), particleModel.data());
            } else {
                if (!(value instanceof SoundModel)) {
                    continue;
                }

                SoundModel soundModel = (SoundModel) value;
                world.playSound(location, soundModel.sound(), soundModel.volume(), soundModel.pitch());
            }
        }
    }

    public void playAt(@NotNull Player player, @Nullable Location location) {
        if (location == null) {
            location = player.getLocation();
        }

        for (Object value : values) {
            if (value instanceof BukkitEffect) {
                BukkitEffect bukkitEffect = (BukkitEffect) value;
                player.playEffect(location, bukkitEffect.effect(), bukkitEffect.data());
            } else if (value instanceof ParticleModel) {
                ParticleModel particleModel = (ParticleModel) value;
                player.spawnParticle(particleModel.particle(), location, particleModel.amount(), particleModel.offX(), particleModel.offY(), particleModel.offZ(), particleModel.extra(), particleModel.data());
            } else {
                if (!(value instanceof SoundModel)) {
                    continue;
                }

                SoundModel soundModel = (SoundModel) value;
                player.playSound(location, soundModel.sound(), soundModel.volume(), soundModel.pitch());
            }
        }
    }

    public CustomEffect clone() {
        return mutableCopy();
    }

    public CustomEffect mutableCopy() {
        return new CustomEffect(values);
    }

    public ImmutableCustomEffect immutableCopy() {
        return new ImmutableCustomEffect(values);
    }

}