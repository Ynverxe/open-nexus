package com.github.ynverxe.dtn.translation;

import java.io.InputStream;
import java.io.IOException;

import org.jetbrains.annotations.Nullable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.github.ynverxe.translation.data.DataSource;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import org.bukkit.plugin.Plugin;
import com.github.ynverxe.translation.data.AbstractFileDataSourceFactory;

public class YamlSourceCreator extends AbstractFileDataSourceFactory {
    private final Plugin plugin;
    
    public YamlSourceCreator(File folder, String format, Plugin plugin) {
        super(folder, format);
        this.plugin = plugin;
    }
    
    protected DataSource consumeExistentFile(@NotNull File file, boolean recentlyCreated) {
        YamlConfiguration yamlConfiguration;

        if (recentlyCreated) {
            InputStream inputStream = this.plugin.getResource(file.getName());
            if (inputStream != null) {
                try {
                    try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        yamlConfiguration = YamlConfiguration.loadConfiguration(reader);
                        yamlConfiguration.save(file);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                yamlConfiguration = new YamlConfiguration();
            }
        } else {
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        return new DataSource() {
            @Nullable
            public Object findData(@NotNull String[] path, char separatorChar) {
                String builtPath = this.buildPath(path, separatorChar);
                if ("".equals(builtPath)) {
                    return null;
                }

                Object found = yamlConfiguration.get(builtPath);
                if (found instanceof ConfigurationSection) {
                    return ((ConfigurationSection)found).getValues(true);
                }

                if (found instanceof ConfigurationSerializable) {
                    return ((ConfigurationSerializable)found).serialize();
                }

                return found;
            }
        };
    }
}
