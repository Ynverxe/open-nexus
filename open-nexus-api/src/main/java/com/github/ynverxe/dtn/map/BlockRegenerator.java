package com.github.ynverxe.dtn.map;

import com.github.ynverxe.dtn.runnable.WaitableAction;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface BlockRegenerator extends Runnable {
    @Nullable WaitableAction regenerationProcessAt(@NotNull Location p0) throws IllegalArgumentException;

    void addBlockToQueue(@NotNull Location p0, int p1) throws IllegalArgumentException;

    @NotNull Map<Location, WaitableAction> allProcesses();
}
