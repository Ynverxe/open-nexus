package com.github.ynverxe.translation;

import com.github.ynverxe.translation.exception.NoEntityHandlerSuchException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityManager {
    @NotNull static EntityManager defaultInstance() {
        return EntityManagerImpl.DEFAULT;
    }

    @NotNull static EntityManager create() {
        return new EntityManagerImpl();
    }

    @NotNull String getLang(@NotNull Object p0) throws NoEntityHandlerSuchException;

    boolean dispatchMessage(@NotNull Object p0, @NotNull Object p1, @NotNull String p2, @NotNull Class<?> p3) throws NoEntityHandlerSuchException;

    @Nullable <T> EntityHandler<T> getHandler(@NotNull Class<T> p0, boolean p1);

    void registerHandlers(@NotNull EntityHandler<?>... p0);
}
