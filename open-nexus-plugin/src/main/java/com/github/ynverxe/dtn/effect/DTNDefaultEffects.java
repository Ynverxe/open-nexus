package com.github.ynverxe.dtn.effect;

import com.github.ynverxe.dtn.model.data.ParticleModel;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.Location;

public final class DTNDefaultEffects {

    public static final ImmutableCustomEffect NEXUS_AGONY_EFFECT = new CustomEffect()
            .addSound(Sound.BLOCK_ANVIL_LAND, 2.3f, 2.4f)
            .addParticle(new ParticleModel(Particle.LAVA, 40, 0.1f, 0.1f, 0.1f, 1.0f, null))
            .immutableCopy();

    public static final CustomEffectFactory<Location> NEXUS_BEAT_EFFECT = location -> new CustomEffect()
            .addParticle(new ParticleModel(Particle.PORTAL, 20, 0.0f, 0.0f, 0.0f, 1.0f))
            .addParticle(new ParticleModel(Particle.ENCHANTMENT_TABLE, 15, 0.0f, 0.0f, 0.0f, 2.0f, null));
    
    private DTNDefaultEffects() {}

}