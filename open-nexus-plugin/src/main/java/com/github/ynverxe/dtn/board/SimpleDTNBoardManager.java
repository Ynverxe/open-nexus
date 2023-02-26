package com.github.ynverxe.dtn.board;

import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.github.ynverxe.dtn.DestroyTheNexus;
import java.util.UUID;
import com.github.ynverxe.util.cache.CacheModel;
import com.github.ynverxe.util.updater.AbstractConjunctEntityUpdater;

public class SimpleDTNBoardManager extends AbstractConjunctEntityUpdater<ComplexBoard> implements DTNBoardManager { private final CacheModel.Mutable<UUID, ComplexBoard> boardCache;
    
    public SimpleDTNBoardManager() {
        super(DestroyTheNexus.LOGGER);
        this.boardCache = CacheModel.create(new ConcurrentHashMap<>());
    }
    
    public void registerBoard(@NotNull UUID uuid, @NotNull ComplexBoard board) {
        this.boardCache.set(uuid, board);
    }
    
    @NotNull
    public ComplexBoard createBoard(@NotNull UUID uuid, @Nullable UpdateMechanism updateMechanism) {
        ComplexBoard complexBoard = this.createAnonymousBoard(updateMechanism);
        this.registerBoard(uuid, complexBoard);

        return complexBoard;
    }
    
    @NotNull
    public ComplexBoard createAnonymousBoard(UpdateMechanism updateMechanism) {
        updateMechanism = ((updateMechanism != null) ? updateMechanism : UpdateMechanism.MOST_LINES);
        BoardEntryHandler boardEntryHandler;
        switch (updateMechanism) {
            case TEAMS: {
                boardEntryHandler = new TeamEntryHandler();
                break;
            }
            case SCORES: {
                boardEntryHandler = new ScoreEntryUpdater();
                break;
            }
            case MOST_LINES: {
                BoardLimitation limitation = BoardLimitation.CURRENT;
                int maxScoreLineLength = limitation.getMaxScoreChars();
                int maxTeamLineLength = limitation.getMaxTeamPartChars() * 2;

                if (maxScoreLineLength > maxTeamLineLength) {
                    return this.createAnonymousBoard(UpdateMechanism.SCORES);
                }
                return this.createAnonymousBoard(UpdateMechanism.TEAMS);
            }
            default: {
                return this.createAnonymousBoard(UpdateMechanism.MOST_LINES);
            }
        }

        return new SimpleComplexBoard(boardEntryHandler, UUID.randomUUID());
    }
    
    @Nullable
    public ComplexBoard get(@NotNull UUID key) {
        return this.boardCache.get(key);
    }
    
    @NotNull
    public Optional<ComplexBoard> safeGet(@NotNull UUID key) {
        return this.boardCache.safeGet(key);
    }
    
    @NotNull
    public Set<UUID> keys() {
        return Collections.unmodifiableSet(this.boardCache.keys());
    }
    
    @NotNull
    public Collection<ComplexBoard> values() {
        return Collections.unmodifiableCollection(this.boardCache.values());
    }
    
    @NotNull
    public Set<Map.Entry<UUID, ComplexBoard>> entries() {
        return Collections.unmodifiableSet(this.boardCache.entries());
    }
    
    public boolean has(@NotNull UUID key) {
        return this.boardCache.has(key);
    }
    
    public int cachedSize() {
        return this.boardCache.cachedSize();
    }
    
    @NotNull
    protected String updaterId() {
        return "DTNBoardUpdater";
    }
    
    protected void consumeEntity(@NotNull ComplexBoard board) {
        if (board.isDisplayed()) {
            board.run();
        }
    }
    
    @NotNull
    protected Collection<ComplexBoard> entities() {
        return this.boardCache.values();
    }
    
    @NotNull
    protected Object resolveEntityId(@NotNull ComplexBoard entity) {
        return entity.id();
    }
    
    @NotNull
    protected String problematicEntityDebugMessage(ComplexBoard board) {
        Player player = board.player();
        return "Board of '" + player.getName() + "' with id '{0}' caused an error, adding it to the black list...";
    }
}
