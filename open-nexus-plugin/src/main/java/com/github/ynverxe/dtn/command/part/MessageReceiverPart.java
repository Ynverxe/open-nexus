package com.github.ynverxe.dtn.command.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.translation.ResourceReceiver;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class MessageReceiverPart implements CommandPart {
    private final String name;
    private final boolean sender;
    
    public MessageReceiverPart(String name, boolean sender) {
        this.name = name;
        this.sender = sender;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        if (this.sender) {
            CommandSender commandSender = context.getObject(CommandSender.class, "SENDER");
            context.setValue(this, ResourceReceiver.ofSender(commandSender));
        } else {
            AnniPlayerPart playerPart = new AnniPlayerPart(this.name, false);

            playerPart.parse(context, stack, part);
            String current = stack.current();

            APlayer aPlayer = (APlayer) context.getValue(playerPart)
                    .orElseThrow(() -> new ArgumentParseException(current + " is not a valid player name"));

            context.setValue(this, aPlayer);
        }
    }
}
