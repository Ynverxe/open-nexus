package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.match.Match;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface APlayer extends PlayerBase {

    @Nullable GameRoom playingRoom();

    @Nullable GameInstance playingGame();

    @Nullable Match playingMatch();

    @NotNull Optional<MatchPlayer> toMatchRepresent();

    default @Nullable EditionInstance editionInstance() {
        return EditionInstance.findByPlayer(this);
    }

    static @NotNull APlayerCache cache() {
        return APlayerCache.instance();
    }

    static @NotNull APlayer resolvePlayer(@NotNull Player player) {
        APlayer aPlayer = resolveUUID(player.getUniqueId());

        if (aPlayer == null)
            throw new IllegalArgumentException("No APlayer registry found for player: " + player);

        return aPlayer;
    }

    static @Nullable APlayer resolveEntity(Entity entity) {
        return (entity instanceof Player) ? resolvePlayer((Player) entity) : null;
    }

    static @Nullable APlayer resolveUUID(@NotNull UUID uuid) {
        return cache().get(uuid);
    }
}
