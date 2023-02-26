package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.translation.LangHolder;
import com.github.ynverxe.dtn.translation.ResourceReceiver;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.dtn.world.Position;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerBase extends ResourceReceiver, LangHolder {
    @NotNull UUID uuid();

    @NotNull String name();

    @NotNull Player bukkitPlayer();

    @NotNull ComplexBoard board();

    @NotNull NamedPosition position();

    @NotNull World world();

    boolean isOnline();

    void setLang(@NotNull String p0);

    default void renderResource(@NotNull Object resource, @Nullable FormattingContext formattingContext) {
        DestroyTheNexus.instance().messenger()
                .dispatchResource(this, resource, (formattingContext != null) ? formattingContext : new FormattingContext());
    }

    void teleport(@NotNull Location location);

    void teleport(@NotNull World world);

    void teleport(@NotNull Position position);

    void teleport(@NotNull World world, @NotNull Position position);

    void teleport(@NotNull NamedPosition position);
}
