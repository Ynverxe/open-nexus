package com.github.ynverxe.translation.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataSource {
    @Nullable Object findData(@NotNull String[] p0, char p1);

    default @NotNull String buildPath(@NotNull String[] path, char separatorChar) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String str : path) {
            builder.append(str);
            if (i < path.length - 1) {
                builder.append(separatorChar);
            }
            ++i;
        }
        return builder.toString();
    }

    interface Factory {
        @NotNull DataSource create(@NotNull String p0);
    }
}
