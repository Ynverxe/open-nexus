package com.github.ynverxe.dtn.command.part;

import com.github.ynverxe.dtn.game.GameInstance;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import net.kyori.text.format.TextColor;
import net.kyori.text.TextComponent;
import com.github.ynverxe.dtn.game.GameRoom;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class GameInstancePart implements CommandPart {
    private final String name;
    
    public GameInstancePart(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        GameRoomPart roomPart = new GameRoomPart(this.name);
        roomPart.parse(context, stack, part);

        GameRoom room = (GameRoom) context.getValue(roomPart).orElse(null);

        if (room == null) {
            return;
        }

        GameInstance game = room.instance();
        if (game == null) {
            throw new ArgumentParseException(TextComponent.of("Room is disabled.").color(TextColor.RED));
        }
        context.setValue(this, game);
    }
}
