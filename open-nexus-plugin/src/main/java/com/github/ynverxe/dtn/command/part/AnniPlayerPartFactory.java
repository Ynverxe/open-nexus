package com.github.ynverxe.dtn.command.part;

import com.github.ynverxe.dtn.annotation.CSender;
import me.fixeddev.commandflow.part.CommandPart;
import java.lang.annotation.Annotation;
import java.util.List;
import me.fixeddev.commandflow.annotated.part.PartFactory;

public class AnniPlayerPartFactory implements PartFactory {
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        boolean sender = false;
        for (Annotation annotation : list) {
            if (annotation.annotationType().equals(CSender.class)) {
                sender = true;
                break;
            }
        }
        return new AnniPlayerPart(s, sender);
    }
}
