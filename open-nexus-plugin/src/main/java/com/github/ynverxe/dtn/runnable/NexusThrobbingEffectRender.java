package com.github.ynverxe.dtn.runnable;

import com.github.ynverxe.dtn.effect.CustomEffect;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.effect.DTNDefaultEffects;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.util.Pair;
import com.github.ynverxe.util.time.Temporizer;
import java.util.concurrent.TimeUnit;
import com.github.ynverxe.dtn.match.Match;

public class NexusThrobbingEffectRender extends SimpleTemporizedRunnable {
    private final Match match;
    
    public NexusThrobbingEffectRender(Match match) {
        super(new Temporizer(null, 1L, TimeUnit.SECONDS), new Pair<>(50L, TimeUnit.MILLISECONDS), true);
        this.match = match;
    }
    
    public void performRun() {
        for (Team value : this.match.teams().values()) {
            final Nexus nexus = value.nexus();
            final CustomEffect effect = DTNDefaultEffects.NEXUS_BEAT_EFFECT.create(nexus.location());
            effect.playAt(nexus.location());
        }
    }
}
