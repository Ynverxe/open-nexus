package com.github.ynverxe.dtn.command.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import com.github.ynverxe.dtn.game.GameRoom;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import com.github.ynverxe.dtn.game.GameManager;
import me.fixeddev.commandflow.part.CommandPart;

public class GameRoomPart implements CommandPart {
    public static final String INVALID_ID = "&cInvalid game id '%s'.";
    private final String name;
    private final GameManager gameManager;
    
    public GameRoomPart(String name) {
        this.name = name;
        this.gameManager = GameManager.instance();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        if (stack.hasNext()) {
            String arg = stack.next();
            GameRoom gameRoom = this.gameManager.get(arg);

            if (gameRoom == null) {
                throw new ArgumentParseException(String.format("&cInvalid game id '%s'.", arg));
            }

            context.setValue(this, gameRoom);
        }
    }
}
