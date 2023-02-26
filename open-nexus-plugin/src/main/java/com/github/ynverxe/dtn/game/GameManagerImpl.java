package com.github.ynverxe.dtn.game;

import java.util.Collection;
import java.util.Set;

import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import com.github.ynverxe.dtn.environment.DTNEnvironment;
import java.util.concurrent.CompletableFuture;
import java.util.Objects;
import java.util.logging.Level;
import com.github.ynverxe.dtn.dimension.Dimension;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;
import com.github.ynverxe.dtn.match.Match;

import java.util.List;
import com.github.ynverxe.dtn.game.type.GameType;
import java.util.Collections;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.database.DirectoryDatabaseHandler;
import com.github.ynverxe.dtn.storage.GsonMapper;
import com.github.ynverxe.dtn.gson.GlobalGson;
import java.util.concurrent.ConcurrentHashMap;
import com.github.ynverxe.dtn.game.expansion.GameExpansion;
import java.util.Map;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;
import java.util.logging.Logger;

public class GameManagerImpl implements GameManager {
    private final Logger logger;
    private final PlainTextModelRepository<GameRoomModel> gameRoomModelRepository;
    private final Map<String, GameRoom> gameRoomMap;
    private final Map<String, GameExpansion> expansionMap;
    
    public GameManagerImpl() {
        this.gameRoomMap = new ConcurrentHashMap<>();
        this.expansionMap = new ConcurrentHashMap<>();
        this.gameRoomModelRepository = PlainTextModelRepository.createRepository(
                new GsonMapper<>(GlobalGson.BASE, GameRoomModel.class),
                DirectoryDatabaseHandler.onPluginFolder("games", ".json")
        );
        this.logger = DestroyTheNexus.LOGGER;
        this.addExpansions(GameExpansion.create(
                new GameType("default", Collections.singletonList("The default DTN game expansion")),
                DefaultPhaseHandler::new,
                new DefaultTieBreaker(),
                null,
                null
        ));
    }
    
    @NotNull
    public List<Match> runningMatches() {
        return this.gameRoomMap.values()
                .stream()
                .filter(room -> room.enabled() && room.instance().runningMatch() != null)
                .map(room -> room.instance().runningMatch())
                .collect(Collectors.toList());
    }
    
    @NotNull
    public List<GameInstance> activeInstances() {
        return this.gameRoomMap.values().stream()
                .filter(GameRoom::enabled)
                .map(GameRoom::instance).collect(Collectors.toList());
    }
    
    @NotNull
    public Optional<GameInstance> instanceOf(@NotNull String gameId) {
        return this.safeGet(gameId).map(GameRoom::instance);
    }
    
    @NotNull
    public Optional<Match> matchOf(@NotNull String gameId) {
        return this.instanceOf(gameId).map(GameInstance::runningMatch);
    }
    
    @Nullable
    public GameRoom get(@NotNull String key) {
        return this.gameRoomMap.get(key);
    }
    
    @NotNull
    public Optional<GameRoom> safeGet(@NotNull String key) {
        return Optional.ofNullable(this.get(key));
    }
    
    @NotNull
    public GameRoom createRoom(@NotNull GameRoomModel model) throws IllegalArgumentException {
        String name = model.name();
        if (this.gameRoomMap.containsKey(name)) {
            throw new IllegalStateException("There's already a room with that name");
        }

        String typeName = model.typeName();
        GameExpansion expansion = this.expansionMap.get(typeName);
        if (expansion == null) {
            throw new IllegalArgumentException("No expansion found for type name: '" + typeName + "'");
        }

        String dimensionName = model.dimensionName();
        Dimension dimension = Dimension.manager().get(dimensionName);
        if (dimension == null) {
            throw new IllegalArgumentException("No dimension found, name: " + dimensionName);
        }

        GameRoom gameRoom = new GameRoomImpl(
                name,
                model.creationDate(),
                typeName,
                expansion,
                model.rules().copy(),
                model.enabled(),
                this
        );

        gameRoom.rebuildLobby(dimension);
        this.gameRoomMap.put(name, gameRoom);
        this.logger.log(Level.INFO, "&aGameRoom[&b%s, %s] loaded/created!", new Object[] { name, dimensionName });
        return gameRoom;
    }
    
    @NotNull
    public GameManager addExpansions(@NotNull GameExpansion... expansions) throws IllegalStateException {
        for (GameExpansion expansion : expansions) {
            String typeName = expansion.gameType().name();
            Objects.requireNonNull(typeName, "typeName");
            if (this.expansionMap.containsKey(typeName)) {
                throw new IllegalArgumentException("there's already a expansion with that name");
            }
            this.expansionMap.put(typeName, Objects.requireNonNull(expansion, "expansion"));
        }

        return this;
    }
    
    @NotNull
    public Optional<GameExpansion> findExpansion(@NotNull String expansionName) {
        return Optional.ofNullable(this.expansionMap.get(expansionName));
    }
    
    public boolean terminateRoom(@NotNull String key) {
        GameRoomImpl gameRoom = (GameRoomImpl)this.get(key);

        if (gameRoom != null) gameRoom.terminate();

        return gameRoom != null;
    }
    
    public boolean saveGame(@NotNull String gameId) {
        GameRoom gameRoom = this.gameRoomMap.get(gameId);
        if (gameRoom == null) {
            return false;
        }

        GameRoomModel model = GameRoomModel.fromRoom(gameRoom);
        this.gameRoomModelRepository.save(gameRoom.name(), model);
        this.logger.log(Level.INFO, "&aGameRoom[&b%s, %s] saved!", new Object[] { gameId, model.dimensionName() });
        return true;
    }
    
    @NotNull
    public PlainTextModelRepository<GameRoomModel> gameRoomModelRepository() {
        return this.gameRoomModelRepository;
    }
    
    public CompletableFuture<Boolean> saveGameAsync(@NotNull String gameId) {
        return CompletableFuture.supplyAsync(() -> this.saveGame(gameId), DTNEnvironment.instance().STORAGE_EXECUTOR)
                .exceptionally(ExceptionCatcher.asHandler());
    }
    
    public CompletableFuture<Void> saveAll(boolean async) {
        Runnable runnable = () -> keys().forEach(this::saveGame);

        if (async) {
            return CompletableFuture.runAsync(runnable, DTNEnvironment.instance().STORAGE_EXECUTOR);
        }

        runnable.run();
        return CompletableFuture.completedFuture(null);
    }
    
    @NotNull
    public Set<String> keys() {
        return Collections.unmodifiableSet(this.gameRoomMap.keySet());
    }
    
    @NotNull
    public Collection<GameRoom> values() {
        return Collections.unmodifiableCollection(this.gameRoomMap.values());
    }
    
    @NotNull
    public Set<Map.Entry<String, GameRoom>> entries() {
        return Collections.unmodifiableSet(this.gameRoomMap.entrySet());
    }
    
    public boolean has(@NotNull String key) {
        return this.gameRoomMap.containsKey(key);
    }
    
    public int cachedSize() {
        return this.gameRoomMap.size();
    }

    void removeRoom(String id) {
        gameRoomMap.remove(id);
    }
}
