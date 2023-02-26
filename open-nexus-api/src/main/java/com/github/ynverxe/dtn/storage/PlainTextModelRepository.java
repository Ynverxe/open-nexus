package com.github.ynverxe.dtn.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface PlainTextModelRepository<T> {

    @NotNull Optional<String> findModelData(@NotNull String p0);

    @NotNull Optional<T> find(@NotNull String p0);

    @NotNull String save(@NotNull String p0, @NotNull T p1);

    void delete(@NotNull String p0);

    @NotNull Map<String, String> groupAllModelData();

    @NotNull Map<String, T> groupAll();

    static @NotNull <T> PlainTextModelRepository<T> createRepository(
            @NotNull Mapper<T> modelMapper,
            @NotNull DatabaseHandler databaseHandler
    ) {
        return new PlainTextModelRepositoryImpl<>(Objects.requireNonNull(modelMapper, "modelMapper"), Objects.requireNonNull(databaseHandler, "databaseHandler"));
    }

    interface DatabaseHandler {
        @Nullable String findModelText(@NotNull String p0);

        void saveModelText(@NotNull String p0, @NotNull String p1);

        void deleteModelText(@NotNull String p0);

        @NotNull Map<String, String> allModels();
    }

    interface Mapper<T> {
        @NotNull T consumeData(@NotNull String p0);

        @NotNull String serializeModel(@NotNull T p0);
    }
}
