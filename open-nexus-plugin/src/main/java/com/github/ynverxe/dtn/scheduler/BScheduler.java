package com.github.ynverxe.dtn.scheduler;

import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import java.util.function.Supplier;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.Plugin;

public class BScheduler implements Scheduler {
    private final Plugin plugin;
    private final BukkitScheduler bukkitScheduler;
    
    public BScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.bukkitScheduler = Bukkit.getScheduler();
    }
    
    public void executeTask(Runnable runnable, int delay, boolean async) {
        if (async) {
            this.bukkitScheduler.runTaskLaterAsynchronously(this.plugin, runnable, delay);
        }
        else {
            this.bukkitScheduler.runTaskLater(this.plugin, runnable, delay);
        }
    }
    
    public CompletableFuture<Void> executeTaskAsFuture(Runnable runnable, int delay, boolean async) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        this.executeTask(() -> {
            try {
                runnable.run();
                completableFuture.complete(null);
            } catch (Throwable e) {
                completableFuture.completeExceptionally(e);
            }
        }, delay, async);

        return completableFuture;
    }
    
    public void executeTaskConstantly(Runnable runnable, int delay, int interval, boolean async) {
        if (async) {
            this.bukkitScheduler.runTaskTimerAsynchronously(this.plugin, runnable, delay, interval);
        } else {
            this.bukkitScheduler.runTaskTimer(this.plugin, runnable, delay, interval);
        }
    }
    
    public <T> CompletableFuture<T> supply(Supplier<T> supplier, int delay, boolean async) {
        CompletableFuture<T> future = new CompletableFuture<T>().exceptionally(ExceptionCatcher.asHandler());
        this.executeTask(() -> future.complete(supplier.get()), delay, async);
        return future;
    }
    
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
