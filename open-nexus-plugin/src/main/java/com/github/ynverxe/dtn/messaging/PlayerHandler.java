package com.github.ynverxe.dtn.messaging;

import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.util.Version;
import com.github.ynverxe.dtn.title.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import com.github.ynverxe.dtn.player.APlayer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import com.github.ynverxe.translation.EntityHandler;

public class PlayerHandler implements EntityHandler<Player> {

    @NotNull
    public String getLang(@NotNull Player entity) {
        return APlayer.resolvePlayer(entity).lang();
    }
    
    public boolean displayMessage(@NotNull Player entity, @NotNull Object message, @NotNull String mode, @NotNull Class<?> messageClass) {
        if (message instanceof BaseComponent) {
            entity.spigot().sendMessage((BaseComponent)message);
        } else if (message instanceof Title) {
            Version version = Version.current();
            int current = version.minorVersionNumber();

            Title title = (Title)message;
            title = title.colorize();
            if (current < 11) {
                entity.sendTitle(title.title(), title.subTitle());
            } else {
                entity.sendTitle(
                        title.title(),
                        title.subTitle(),
                        title.fadeIn(),
                        title.stay(),
                        title.fadeOut()
                );
            }
        } else {
            entity.sendMessage(message.toString());
        }

        System.out.println(message);
        return true;
    }
    
    @Nullable
    public Object transformEntity(@NotNull Player entity) {
        return null;
    }
    
    @NotNull
    public Class<Player> representingType() {
        return Player.class;
    }
}
