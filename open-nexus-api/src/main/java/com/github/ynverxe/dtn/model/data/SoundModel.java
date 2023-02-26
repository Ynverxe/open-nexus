package com.github.ynverxe.dtn.model.data;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundModel {

    private final @NotNull Sound sound;
    private final float volume;
    private final float pitch;

    public SoundModel(@NotNull Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public @NotNull Sound sound() {
        return sound;
    }

    public float volume() {
        return volume;
    }

    public float pitch() {
        return pitch;
    }

}
