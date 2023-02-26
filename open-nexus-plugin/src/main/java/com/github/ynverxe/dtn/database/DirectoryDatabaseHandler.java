package com.github.ynverxe.dtn.database;

import com.github.ynverxe.dtn.environment.DTNEnvironment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.io.IOException;
import java.io.FileWriter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import com.github.ynverxe.dtn.storage.PlainTextModelRepository;

public class DirectoryDatabaseHandler implements PlainTextModelRepository.DatabaseHandler {

    protected final File directory;
    protected final String format;
    
    public DirectoryDatabaseHandler(File directory, String format) {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("unable to create directory");
        }
        this.directory = directory;
        this.format = (format.startsWith(".") ? format : ("." + format));
    }

    public @Nullable String findModelText(@NotNull String key) {
        File file = this.findFile(key, false);
        if (file.exists()) {
            return null;
        }

        return this.getText(file);
    }
    
    public void saveModelText(@NotNull String key, @NotNull String modelText) {
        final File file = this.findFile(key, true);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(modelText);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void deleteModelText(@NotNull String key) {
        final File file = this.findFile(key, false);
        if (!file.delete()) {
            throw new RuntimeException("unable to delete file: '" + key + "'");
        }
    }

    public @NotNull Map<String, String> allModels() {
        File[] files = this.directory.listFiles((dir, name) -> name.endsWith(this.format));
        if (files == null) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new HashMap<>();
        for (File file : files) {
            map.put(file.getName().replace(this.format, ""), this.getText(file));
        }

        return map;
    }
    
    private String getText(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private File findFile(String name, boolean create) {
        name = (name.endsWith(this.format) ? name : (name + this.format));
        File file = new File(this.directory, name);

        if (!file.exists() && create) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("unable to create file '" + name + "'");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }
    
    @NotNull
    public static DirectoryDatabaseHandler onPluginFolder(@NotNull String dirName, @NotNull String format) {
        return new DirectoryDatabaseHandler(new File(DTNEnvironment.instance().pluginFolder, dirName), format);
    }
}
