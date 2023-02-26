package com.github.ynverxe.translation;

import com.github.ynverxe.translation.data.TranslationDataProvider;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import org.jetbrains.annotations.NotNull;

public interface Messenger {
    static @NotNull Messenger create(@NotNull EntityManager entityManager, @NotNull ResourceMapper resourceMapper) {
        return new MessengerImpl(entityManager, resourceMapper);
    }

    boolean dispatchResource(@NotNull Object p0, @NotNull Object p1, @NotNull FormattingContext p2);

    default boolean dispatchResource(@NotNull Object entity, @NotNull Object resource) {
        return dispatchResource(entity, resource, new FormattingContext());
    }

    @NotNull ResourceMapper resourceMapper();

    @NotNull EntityManager entityManager();

    @NotNull TranslationDataProvider translationDataProvider();
}
