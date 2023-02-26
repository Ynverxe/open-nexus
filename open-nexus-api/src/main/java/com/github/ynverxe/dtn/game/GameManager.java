package com.github.ynverxe.dtn.game;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.game.expansion.GameExpansion;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;
import com.github.ynverxe.util.cache.CacheModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GameManager extends CacheModel<String, GameRoom> {
    @NotNull List<Match> runningMatches();

    @NotNull List<GameInstance> activeInstances();

    @NotNull Optional<GameInstance> instanceOf(@NotNull String gameRoomId);

    @NotNull Optional<Match> matchOf(@NotNull String gameRoomId);

    @NotNull GameRoom createRoom(@NotNull GameRoomModel roomModel) throws IllegalArgumentException;

    @NotNull GameManager addExpansions(@NotNull GameExpansion... gameExpansions) throws IllegalStateException;

    @NotNull Optional<GameExpansion> findExpansion(@NotNull String expansionTypeName);

    boolean terminateRoom(@NotNull String gameRoomId);

    boolean saveGame(@NotNull String gameRoomId);

    @NotNull PlainTextModelRepository<GameRoomModel> gameRoomModelRepository();

    CompletableFuture<Boolean> saveGameAsync(@NotNull String gameRoomId);

    CompletableFuture<Void> saveAll(boolean save);

    static @NotNull GameManager instance() {
        return DestroyTheNexus.instance().gameManager();
    }
}
