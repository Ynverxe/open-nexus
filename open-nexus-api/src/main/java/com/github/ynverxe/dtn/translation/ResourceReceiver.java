package com.github.ynverxe.dtn.translation;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.translation.Messenger;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResourceReceiver {
    static @NotNull ResourceReceiver ofSender(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            return APlayer.resolvePlayer(((Player) commandSender));
        }

        return (message, context) -> {
            Messenger messenger = DestroyTheNexus.instance().messenger();

            if (context == null) context = new FormattingContext();

            messenger.dispatchResource(commandSender, message, context);
        };
    }

    void renderResource(@NotNull Object resource, @Nullable FormattingContext formattingContext);

    default void renderResource(@NotNull Object resource) {
        renderResource(resource, null);
    }
}
