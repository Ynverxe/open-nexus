package com.github.ynverxe.dtn.command.part;

import java.util.HashMap;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.fixeddev.commandflow.CommandContext;
import com.google.gson.Gson;
import me.fixeddev.commandflow.part.CommandPart;

public class MapPart implements CommandPart {

    private static final Gson gson;
    private final String name;
    
    public MapPart(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    @SuppressWarnings("unchecked")
    public void parse(CommandContext context, ArgumentStack stack, @Nullable final CommandPart part) throws ArgumentParseException {
        Map<String, Object> objectMap = null;
        while (stack.hasNext()) {
            String left = stack.next();
            if (!stack.hasNext()) {
                try {
                    objectMap = MapPart.gson.fromJson(left, Map.class);
                    break;
                } catch (Exception e) {
                    throw new ArgumentParseException("Invalid json");
                }
            }

            objectMap = new HashMap<>();
            Object right = stack.next();
            objectMap.put(left, right);
        }

        if (objectMap != null) {
            context.setValue(this, objectMap);
        }
    }
    
    static {
        gson = new Gson();
    }
}
