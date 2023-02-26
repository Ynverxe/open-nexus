package com.github.ynverxe.dtn.command.part;

import java.util.UUID;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.entity.Player;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class AnniPlayerPart implements CommandPart {
    private final String name;
    private final boolean sender;
    
    public AnniPlayerPart(String name, boolean sender) {
        this.name = name;
        this.sender = sender;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        APlayer aPlayer;

        if (this.sender) {
            CommandSender sender = context.getObject(CommandSender.class, "SENDER");

            if (sender == null) {
                throw new ArgumentParseException("No command sender found");
            }

            if (!(sender instanceof Player)) {
                throw new ArgumentParseException("Only players can execute this command");
            }

            aPlayer = APlayer.resolvePlayer((Player)sender);
        } else {
            String arg = stack.current();
            Player player = Bukkit.getPlayer(arg);

            if (player == null) {
                UUID uuid;

                try {
                    uuid = UUID.fromString(arg);
                } catch (Exception e) {
                    uuid = null;
                }

                if (uuid == null) {
                    throw new ArgumentParseException("Unable to parse '" + arg + "' as player.");
                }

                player = Bukkit.getPlayer(uuid);
            }

            aPlayer = player != null ? APlayer.resolvePlayer(player) : null;
        }

        context.setValue(this, aPlayer);
    }
}
