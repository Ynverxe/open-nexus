package com.github.ynverxe.dtn.map;

import org.bukkit.block.BlockState;
import java.util.Collections;
import org.bukkit.block.Block;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import com.github.ynverxe.dtn.runnable.WaitableAction;
import org.bukkit.Location;
import java.util.Map;

public class BlockRegeneratorImpl implements BlockRegenerator {
    private final MatchMapImpl matchMap;
    private final Map<Location, WaitableAction> actionsQueue;
    
    BlockRegeneratorImpl(MatchMapImpl matchMap) {
        this.actionsQueue = new ConcurrentHashMap<>();
        this.matchMap = matchMap;
    }

    public @Nullable WaitableAction regenerationProcessAt(@NotNull Location location) {
        this.matchMap.checkNotTerminated0();
        this.matchMap.checkNotExternal(location);

        location = location.getBlock().getLocation();

        return this.actionsQueue.get(location);
    }
    
    public void addBlockToQueue(@NotNull Location location, int regenTime) {
        Block block = location.getBlock();
        long totalWaitingMillis = TimeUnit.SECONDS.toMillis(regenTime);

        this.actionsQueue.put(block.getLocation(), new WaitableAction(new RegenerationPerformer(block), totalWaitingMillis));
    }
    
    @NotNull
    public Map<Location, WaitableAction> allProcesses() {
        return Collections.unmodifiableMap(this.actionsQueue);
    }
    
    public void run() {
        for (Map.Entry<Location, WaitableAction> entry : this.actionsQueue.entrySet()) {
            WaitableAction action = entry.getValue();

            if (System.currentTimeMillis() >= action.timeGoal()) {
                action.run();
            }
        }
    }
    
    private class RegenerationPerformer implements Runnable {
        private final BlockState blockState;
        
        public RegenerationPerformer(Block block) {
            this.blockState = block.getState();
        }
        
        @Override
        public void run() {
            BlockRegeneratorImpl.this.actionsQueue.remove(this.blockState.getBlock().getLocation());
            this.blockState.update(true, true);
        }
    }
}
