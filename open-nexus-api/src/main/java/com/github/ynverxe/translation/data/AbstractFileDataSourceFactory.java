package com.github.ynverxe.translation.data;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFileDataSourceFactory implements DataSource.Factory {
    private final File folder;
    private final String format;

    public AbstractFileDataSourceFactory(File folder, String format) {
        this.folder = folder;
        this.format = format;
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalArgumentException("Unable to create folder: " + folder.getName());
        }
    }

    @NotNull @Override
    public DataSource create(@NotNull String sourceName) {
        final String fileName = format.replace("<lang>", sourceName);
        final File file = new File(folder, fileName);
        boolean recentlyCreated = false;
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IllegalArgumentException("Unable to create file:" + fileName);
                }
                recentlyCreated = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return consumeExistentFile(file, recentlyCreated);
    }

    protected abstract DataSource consumeExistentFile(@NotNull File p0, boolean p1);
}
