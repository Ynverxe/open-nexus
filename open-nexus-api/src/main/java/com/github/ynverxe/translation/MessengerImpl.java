package com.github.ynverxe.translation;

import com.github.ynverxe.translation.data.TranslationDataProvider;
import com.github.ynverxe.translation.resource.ResourceReference;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessengerImpl implements Messenger {

    private final EntityManager entityManager;
    private final ResourceMapper resourceMapper;

    public MessengerImpl(EntityManager entityManager, ResourceMapper resourceMapper) {
        this.entityManager = entityManager;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public boolean dispatchResource(@NotNull Object entity, @NotNull Object resource, @NotNull FormattingContext context) {
        ResourceReference<?> typedResourceReference = ResourceReference.resolve(resource);
        if (entity instanceof Iterable) {
            List<Object> entities = new ArrayList<>();
            ((Iterable<?>) entity).forEach(entities::add);

            boolean allEntitiesCorrectlyProcessed = true;
            for (Object o : entities) {
                if (!dispatchResource(o, resource, context) && allEntitiesCorrectlyProcessed) {
                    allEntitiesCorrectlyProcessed = false;
                }
            }

            return allEntitiesCorrectlyProcessed;
        }

        String lang = entityManager.getLang(entity);
        context.setSourceName(lang);
        context.setReceptor(entity);
        List<?> messageToDispatch = resourceMapper.formatResource(typedResourceReference, context);
        return sendToEntity(entity, messageToDispatch, typedResourceReference);
    }

    @Override
    public @NotNull ResourceMapper resourceMapper() {
        return resourceMapper;
    }

    @NotNull @Override
    public EntityManager entityManager() {
        return entityManager;
    }

    @NotNull @Override
    public TranslationDataProvider translationDataProvider() {
        return resourceMapper.translationDataProvider();
    }

    private boolean sendToEntity(Object entity, Collection<?> messages, ResourceReference<?> reference) {
        boolean b = true;
        for (Object o : messages) {
            if (o instanceof Collection) {
                b = sendToEntity(entity, (Collection<?>) o, reference);
            } else {
                if (entityManager.dispatchMessage(entity, o, reference.renderMode(), o.getClass())) {
                    continue;
                }
                b = false;
            }
        }
        return b;
    }
}
