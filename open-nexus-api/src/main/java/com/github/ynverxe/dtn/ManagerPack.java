package com.github.ynverxe.dtn;

import com.github.ynverxe.dtn.board.DTNBoardManager;
import com.github.ynverxe.dtn.currency.CurrencyManager;
import com.github.ynverxe.dtn.dimension.DimensionManager;
import com.github.ynverxe.dtn.dimension.edition.EditionInstanceManager;
import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import com.github.ynverxe.dtn.game.GameInstanceUpdater;
import com.github.ynverxe.dtn.game.GameManager;
import com.github.ynverxe.dtn.kit.KitRegistry;
import com.github.ynverxe.dtn.player.APlayerCache;
import com.github.ynverxe.dtn.scheduler.Scheduler;
import com.github.ynverxe.dtn.tickable.TickableRegistry;
import com.github.ynverxe.dtn.world.WorldHelper;
import com.github.ynverxe.translation.Messenger;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import org.jetbrains.annotations.NotNull;

public interface ManagerPack {

    @NotNull Messenger messenger();

    @NotNull ResourceMapper resourceMapper();

    @NotNull APlayerCache playerCache();

    @NotNull GameManager gameManager();

    @NotNull DimensionManager dimensionManager();

    @NotNull EditionInstanceManager configurationInstanceManager();

    @NotNull Scheduler scheduler();

    @NotNull DTNBoardManager boardManager();

    @NotNull GameInstanceUpdater gameInstanceUpdater();

    @NotNull KitRegistry kitRegistry();

    @NotNull TickableRegistry tickableRegistry();

    @NotNull CurrencyManager currencyManager();

    @NotNull WorldHelper worldHelper();

    @NotNull ExceptionCatcher exceptionCatcher();

}
