package com.github.ynverxe.dtn.command.part;

import java.util.ArrayList;
import java.util.List;

import com.github.ynverxe.dtn.boss.BossType;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import net.kyori.text.format.TextColor;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class BossTypePart implements CommandPart {

    private final String name;
    
    public BossTypePart(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        String next = stack.next();

        BossType bossType = BossType.REGISTRY.get(next);

        if (bossType == null) {
            throw new ArgumentParseException(TextComponent.of("Invalid boss type name.").color(TextColor.RED));
        }

        context.setValue(this, bossType);
    }
    
    @Nullable
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return new ArrayList<>(BossType.REGISTRY.keys());
    }
}
