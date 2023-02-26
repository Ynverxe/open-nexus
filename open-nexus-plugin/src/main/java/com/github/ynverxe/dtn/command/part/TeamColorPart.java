package com.github.ynverxe.dtn.command.part;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import com.github.ynverxe.dtn.team.TeamColor;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class TeamColorPart implements CommandPart {
    private static final String INVALID_NAME = "&cInvalid team color name.";
    private final String name;
    
    public TeamColorPart(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        final String arg = stack.next();
        try {
            final TeamColor teamColor = TeamColor.byName(arg);
            context.setValue(this, teamColor);
        }
        catch (Exception e) {
            throw new ArgumentParseException(TextComponent.of("&cInvalid team color name.", TextColor.RED));
        }
    }
    
    @Nullable
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return Arrays.stream(TeamColor.values())
                .map(TeamColor::lowerCaseName)
                .collect(Collectors.toList());
    }
}
