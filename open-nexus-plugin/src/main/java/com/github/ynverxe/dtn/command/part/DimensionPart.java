package com.github.ynverxe.dtn.command.part;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import com.github.ynverxe.dtn.dimension.Dimension;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.CommandPart;

public class DimensionPart implements CommandPart {

    public static final String INVALID_DIMENSION_NAME = "Dimension '%s' doesn't exists.";
    private final String name;
    
    public DimensionPart(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart part) throws ArgumentParseException {
        String dimensionName = stack.next();

        Dimension dimension = Dimension.manager().safeGet(dimensionName)
                .orElseThrow(() -> new ArgumentParseException(String.format("Dimension '%s' doesn't exists.", dimensionName)));

        context.setValue(this, dimension);
    }
}
