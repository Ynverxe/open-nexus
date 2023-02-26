package com.github.ynverxe.dtn.command;

import com.github.ynverxe.dtn.DestroyTheNexus;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.CommandClass;

@Command(names = "dtn", permission = "dtn.admin")
public class DTNCommand implements CommandClass {
    @Command(names = "refresh-updaters", permission = "dtn.admin.refreshupdaters")
    public boolean refreshUpdaters() {
        final DestroyTheNexus destroyTheNexus = DestroyTheNexus.instance();
        destroyTheNexus.boardManager().clearProblematicEntities();
        destroyTheNexus.gameInstanceUpdater().clearProblematicEntities();
        return true;
    }
}
