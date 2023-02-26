package com.github.ynverxe.dtn.environment;

import com.github.ynverxe.dtn.exception.SingletonClassException;
import com.github.ynverxe.util.TextColorizer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public final class DTNEnvironment {

    public static final String LOG_PREFIX = "[DestroyTheNexus]";
    public static final String COLORED_LOG_PREFIX = TextColorizer.colorize("&a[&6DestroyTheNexys&a]&r");
    private static final ThreadGroup STORAGE_THREAD_GROUP;
    private static final ThreadGroup WORLD_THREAD_GROUP;
    private static DTNEnvironment ENVIRONMENT_OPTIONS;

    static {
        STORAGE_THREAD_GROUP = new ThreadGroup("DTN-Storage");
        WORLD_THREAD_GROUP = new ThreadGroup("World-Loader");
    }

    public final File pluginFolder;
    public final File optionsFile;
    public final Properties properties;
    public final Executor STORAGE_EXECUTOR;
    public final Executor WORLD_LOADER_EXECUTOR;

    public DTNEnvironment(File pluginFolder) throws Throwable {
        if (DTNEnvironment.ENVIRONMENT_OPTIONS != null) {
            throw new SingletonClassException(getClass());
        }
        DTNEnvironment.ENVIRONMENT_OPTIONS = this;
        this.pluginFolder = pluginFolder;
        optionsFile = new File(pluginFolder, "dtn-options.properties");
        try {
            if (!optionsFile.exists() && !optionsFile.createNewFile()) {
                throw new RuntimeException("unable to create options file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        properties = new Properties();
        loadOptions();

        Function<ThreadGroup, ThreadFactory> factoryFunction = threadGroup -> r -> {
            Thread thread = new Thread(threadGroup, r);
            thread.setDaemon(true);
            return thread;
        };

        STORAGE_EXECUTOR = Executors.newFixedThreadPool(maxStorageThreads(), factoryFunction.apply(DTNEnvironment.STORAGE_THREAD_GROUP));
        WORLD_LOADER_EXECUTOR = Executors.newFixedThreadPool(maxWorldLoaderThreads(), factoryFunction.apply(DTNEnvironment.WORLD_THREAD_GROUP));
    }

    public @NotNull static DTNEnvironment instance() {
        return DTNEnvironment.ENVIRONMENT_OPTIONS;
    }

    private void loadOptions() throws IOException {
        properties.clear();
        properties.load(new FileReader(optionsFile));
    }

    public @NotNull String lobbyWorld() {
        return properties.getProperty("lobby-world");
    }

    public int maxStorageThreads() {
        final int def = Runtime.getRuntime().availableProcessors() * 2;
        return Integer.decode(properties.getProperty("storage-threads", def + ""));
    }

    public int maxWorldLoaderThreads() {
        return Integer.decode(properties.getProperty("world-loader-threads", "2"));
    }
}
