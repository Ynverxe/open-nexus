package com.github.ynverxe.dtn.messaging;

import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.ConsoleCommandSender;
import com.github.ynverxe.translation.EntityHandler;

public class ConsoleSenderHandler implements EntityHandler<ConsoleCommandSender> {
    @NotNull
    public String getLang(@NotNull ConsoleCommandSender entity) {
        return Locale.getDefault().getLanguage();
    }
    
    public boolean displayMessage(@NotNull ConsoleCommandSender entity, @NotNull Object message, @NotNull String mode, @NotNull Class<?> messageClass) {
        entity.sendMessage(message.toString());
        return true;
    }
    
    @Nullable
    public Object transformEntity(@NotNull ConsoleCommandSender entity) {
        return null;
    }
    
    @NotNull
    public Class<ConsoleCommandSender> representingType() {
        return ConsoleCommandSender.class;
    }
}
