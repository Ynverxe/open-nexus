package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface KitRegistry extends Iterable<Kit> {

    @NotNull KitBuilder createKitBuilder(@NotNull String kitName, @NotNull Plugin plugin);

    @Nullable Kit findKit(@NotNull String kitName);

    @NotNull List<Kit> getPluginKits(@NotNull Plugin plugin);

    @NotNull Kit defaultKit();

    @NotNull
    static KitRegistry instance() {
        return DestroyTheNexus.instance().kitRegistry();
    }
}
