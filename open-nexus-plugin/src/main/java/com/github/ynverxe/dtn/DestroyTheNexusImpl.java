package com.github.ynverxe.dtn;

import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import com.github.ynverxe.dtn.exception.ExceptionCatcherImpl;
import com.github.ynverxe.dtn.exception.MissingValueException;
import com.github.ynverxe.dtn.exception.SuppresableException;
import com.github.ynverxe.dtn.world.WorldHelper;
import com.github.ynverxe.dtn.world.WorldHelperImpl;
import com.github.ynverxe.structured.exception.PathHolderException;
import org.bukkit.ChatColor;
import com.github.ynverxe.dtn.scheduler.Scheduler;
import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.player.PlayerJoinToLobby;
import com.github.ynverxe.dtn.player.APlayer;
import java.util.Objects;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.dtn.environment.DTNEnvironment;
import com.github.ynverxe.dtn.translation.BoardInterpreter;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.dtn.messaging.MatchPlayerHandler;
import com.github.ynverxe.dtn.messaging.APlayerHandler;
import com.github.ynverxe.dtn.messaging.PlayerHandler;
import com.github.ynverxe.dtn.messaging.ConsoleSenderHandler;
import com.github.ynverxe.translation.EntityManager;
import com.github.ynverxe.translation.data.TranslationDataProvider;
import com.github.ynverxe.dtn.translation.YamlSourceCreator;
import java.io.File;

import com.github.ynverxe.dtn.kit.KitRegistryImpl;
import com.github.ynverxe.dtn.game.GameInstanceUpdaterImpl;
import com.github.ynverxe.dtn.board.SimpleDTNBoardManager;
import com.github.ynverxe.dtn.player.APlayerCacheImpl;
import com.github.ynverxe.dtn.edition.EditionInstanceManagerImpl;
import com.github.ynverxe.dtn.game.GameManagerImpl;
import com.github.ynverxe.dtn.dimension.DimensionManagerImpl;
import com.github.ynverxe.dtn.currency.CurrencyManager;
import com.github.ynverxe.dtn.tickable.TickableRegistry;
import com.github.ynverxe.dtn.kit.KitRegistry;
import com.github.ynverxe.dtn.game.GameInstanceUpdater;
import com.github.ynverxe.dtn.board.DTNBoardManager;
import com.github.ynverxe.dtn.player.APlayerCache;
import com.github.ynverxe.dtn.dimension.edition.EditionInstanceManager;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import com.github.ynverxe.translation.Messenger;
import com.github.ynverxe.dtn.scheduler.BScheduler;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.game.GameManager;

public class DestroyTheNexusImpl implements DestroyTheNexus {

    private World lobbyWorld;

    private final GameManager gameManager;
    private final DimensionManager dimensionManager;
    private final BScheduler scheduler;
    private final Messenger messenger;
    private final ResourceMapper resourceMapper;
    private final EditionInstanceManager editionInstanceManager;
    private final APlayerCache playerCache;
    private final WorldHelper worldHelper;
    private final DTNBoardManager boardManager;
    private final GameInstanceUpdater gameInstanceUpdater;
    private final KitRegistry kitRegistry;
    private final TickableRegistry tickableRegistry;
    private final CurrencyManager currencyManager;
    private final ExceptionCatcher exceptionCatcher;

    private DestroyTheNexusImpl() {
        DestroyTheNexusPlugin plugin = DestroyTheNexusPlugin.plugin();

        this.scheduler = new BScheduler(plugin);

        this.exceptionCatcher = new ExceptionCatcherImpl();
        this.exceptionCatcher.addHandler(SuppresableException.HANDLER);
        this.exceptionCatcher.addHandler(e -> {
            if (e instanceof PathHolderException || e instanceof MissingValueException) {
                String message = "[" + e.getClass().getSimpleName() + "] " + e.getMessage();

                LOGGER.warning(message);
                return true;
            }

            return false;
        });


        this.dimensionManager = new DimensionManagerImpl();
        this.gameManager = new GameManagerImpl();
        this.editionInstanceManager = new EditionInstanceManagerImpl();
        this.playerCache = new APlayerCacheImpl();
        this.worldHelper = new WorldHelperImpl();
        this.boardManager = new SimpleDTNBoardManager();
        this.gameInstanceUpdater = new GameInstanceUpdaterImpl();
        this.kitRegistry = new KitRegistryImpl();
        this.tickableRegistry = new TickableRegistry();
        this.currencyManager = new CurrencyManager();

        File messagesFolder = new File(DestroyTheNexusPlugin.plugin().folder(), "messages");
        TranslationDataProvider translationDataProvider = new TranslationDataProvider(new YamlSourceCreator(messagesFolder, "<lang>.yml", plugin));
        EntityManager entityManager = EntityManager.defaultInstance();
        entityManager.registerHandlers(new ConsoleSenderHandler(), new PlayerHandler(), new APlayerHandler(), new MatchPlayerHandler());

        this.resourceMapper = ResourceMapper.create(translationDataProvider);
        this.resourceMapper.registerStringInterceptors((str, formattingScheme, formattingContext) ->
                ChatColor.translateAlternateColorCodes('&', str));
        this.resourceMapper.registerInterpreter(BoardModel.class, new BoardInterpreter());
        this.messenger = Messenger.create(EntityManager.defaultInstance(), this.resourceMapper);

        String lobbyWorld = DTNEnvironment.instance().lobbyWorld();
        this.lobbyWorld = worldHelper.createWorldIfAbsent(lobbyWorld, false);
    }

    @NotNull
    public APlayerCache playerCache() {
        return this.playerCache;
    }

    @Override
    public @NotNull WorldHelper worldHelper() {
        return worldHelper;
    }

    @NotNull
    public GameManager gameManager() {
        return this.gameManager;
    }

    @NotNull
    public DimensionManager dimensionManager() {
        return this.dimensionManager;
    }

    public @NotNull World lobbyWorld() {
        return this.lobbyWorld;
    }

    public void setLobbyWorld(@NotNull World world) {
        this.lobbyWorld = Objects.requireNonNull(world, "world");
    }

    public void sendToLobby(@NotNull APlayer player) {
        if (!player.isOnline()) {
            return;
        }

        PlayerJoinToLobby event = new PlayerJoinToLobby(player);
        Bukkit.getPluginManager().callEvent(event);
        ComplexBoard board = player.board();

        BoardModel boardModel = player.findTranslationResource(DefaultTranslationContainer.LOBBY_BOARD, null);
        player.teleport(this.lobbyWorld);
        boardModel.applyToBoard(board);
    }

    @NotNull
    public Messenger messenger() {
        return this.messenger;
    }

    @NotNull
    public ResourceMapper resourceMapper() {
        return this.resourceMapper;
    }

    @NotNull
    public EditionInstanceManager configurationInstanceManager() {
        return this.editionInstanceManager;
    }

    @NotNull
    public Scheduler scheduler() {
        return this.scheduler;
    }

    @NotNull
    public DTNBoardManager boardManager() {
        return this.boardManager;
    }

    @NotNull
    public GameInstanceUpdater gameInstanceUpdater() {
        return this.gameInstanceUpdater;
    }

    @NotNull
    public KitRegistry kitRegistry() {
        return this.kitRegistry;
    }

    @NotNull
    public TickableRegistry tickableRegistry() {
        return this.tickableRegistry;
    }

    @NotNull
    public CurrencyManager currencyManager() {
        return this.currencyManager;
    }

    @Override
    public @NotNull ExceptionCatcher exceptionCatcher() {
        return exceptionCatcher;
    }

    public static void initializeAPIClass() {
        DestroyTheNexus destroyTheNexus = null;

        try {
            destroyTheNexus = DestroyTheNexus.instance();
        } catch (Exception ignored) {}

        if (destroyTheNexus != null) {
            throw new UnsupportedOperationException("cannot create another instance of this class");
        }

        destroyTheNexus = new DestroyTheNexusImpl();
        DTNSingletonContainer.setInstance(destroyTheNexus);
    }
}
