package com.github.ynverxe.translation;

import com.github.ynverxe.translation.exception.NoEntityHandlerSuchException;
import com.github.ynverxe.util.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked, rawtypes")
public class EntityManagerImpl implements EntityManager {

    static final EntityManager DEFAULT = new EntityManagerImpl();

    private final Map<Class, EntityHandler> handlerMap;

    public EntityManagerImpl() {
        handlerMap = new HashMap<>();
    }

    @NotNull @Override
    public String getLang(@NotNull Object entity) throws NoEntityHandlerSuchException {
        BiFunction<Object, EntityHandler, String> mapper = ((e, entityHandler) -> entityHandler.getLang(e));
        return findHandler(entity, mapper, true, true);
    }

    @Override
    public boolean dispatchMessage(@NotNull Object entity, @NotNull Object message, @NotNull String mode, @NotNull Class<?> messageClass)
            throws NoEntityHandlerSuchException {
        BiFunction<Object, EntityHandler, Boolean> mapper = ((e, entityHandler) -> entityHandler.displayMessage(e, message, mode, messageClass));
        return findHandler(entity, mapper, true, true);
    }

    @NotNull @Override
    public <T> EntityHandler<T> getHandler(@NotNull Class<T> entityClass, boolean hierarchySearch) {
        return (EntityHandler<T>) findHandler(entityClass, hierarchySearch, false);
    }

    @Override
    public void registerHandlers(@NotNull EntityHandler<?>... entityHandlers) {
        for (EntityHandler<?> entityHandler : entityHandlers) {
            handlerMap.put(entityHandler.representingType(), entityHandler);
        }
    }

    private <T> T findHandler(Object entity, BiFunction<Object, EntityHandler, T> mapper, boolean hierarchySearch, boolean debug) {
        EntityHandler entityHandler = findHandler(entity.getClass(), hierarchySearch, debug);
        Object transformed = entityHandler.transformEntity(entity);

        if (transformed != null) {
            return findHandler(transformed, mapper, hierarchySearch, debug);
        }

        return mapper.apply(entity, entityHandler);
    }

    private EntityHandler findHandler(Class clazz, boolean hierarchySearch, boolean debug) {
        EntityHandler entityHandler = handlerMap.get(clazz);

        if (entityHandler == null && hierarchySearch) {
            entityHandler = Maps.hierarchySearch(clazz, handlerMap);
        }

        if (entityHandler == null && debug) {
            throw new NoEntityHandlerSuchException(clazz);
        }

        return entityHandler;
    }
}
