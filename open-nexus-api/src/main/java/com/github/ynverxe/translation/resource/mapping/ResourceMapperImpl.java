package com.github.ynverxe.translation.resource.mapping;

import com.github.ynverxe.structured.data.ModelDataList;
import com.github.ynverxe.structured.data.ModelDataValue;
import com.github.ynverxe.translation.data.TranslationDataProvider;
import com.github.ynverxe.translation.resource.ResourceOptions;
import com.github.ynverxe.translation.resource.ResourceReference;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked, rawtypes")
public class ResourceMapperImpl implements ResourceMapper {

    private final TranslationDataProvider translationDataProvider;
    private final List<StringInterceptor> stringInterceptors;
    private final Map<String, Class> classTypeNames;
    private final Map<Class, ResourceInterpreter> interpreterMap;

    public ResourceMapperImpl(@NotNull TranslationDataProvider translationDataProvider) {
        stringInterceptors = new ArrayList<>();
        classTypeNames = new HashMap<>();
        interpreterMap = new HashMap<>();
        this.translationDataProvider = translationDataProvider;
        bindClassTypeName("string", String.class);
        registerInterpreter(String.class, new StringInterpreter());
    }

    @NotNull @Override
    public List<String> formatStrings(
            @NotNull List<String> strings,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext
    ) {
        formattingContext.setResourceMapper(this);
        List<String> formatted = new ArrayList<>(strings.size());

        for (String string : strings) {
            for (StringInterceptor stringInterceptor : stringInterceptors) {
                string = stringInterceptor.intercept(string, formattingScheme, formattingContext);
            }

            formatted.add(StringFormatter.format(string, formattingScheme, formattingContext));
        }

        return formatted;
    }

    @NotNull @Override
    public <T> List<T> formatData(
            @NotNull ModelDataValue dataValue,
            @NotNull ResourceOptions resourceOptions,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext context
    ) {
        Class resourceClass = resourceOptions.type();

        if (Object.class.equals(resourceClass)) {
            String typeName = resourceOptions.typeName();

            if (dataValue.isTree()) {
                typeName = dataValue.asTree().safeGet("$typeName")
                        .map(ModelDataValue::asString)
                        .orElse(typeName);
            }

            if (typeName.isEmpty()) {
                throw new IllegalArgumentException("Abstract resource data doesn't has type name");
            }

            resourceClass = classTypeNames.get(typeName);
            if (resourceClass == null) {
                throw new IllegalArgumentException("Unknown class type name: '" + typeName + "'");
            }
        }

        ResourceInterpreter resourceInterpreter = interpreterMap.get(resourceClass);
        if (resourceInterpreter == null) {
            throw new IllegalArgumentException("No resource interpreter found for: " + resourceClass);
        }

        Object value =  resourceInterpreter.buildResource(
                dataValue,
                formattingScheme,
                context,
                this
        );

        if (value == null) {
            throw new IllegalArgumentException("Unable to build resource as: " + resourceClass);
        }

        return (List<T>) ((value instanceof List) ? ((List) value) : Collections.singletonList(value));
    }

    @NotNull @Override
    public <T> List<T> formatResource(@NotNull ResourceReference<T> resourceReference, @NotNull FormattingContext context) {
        String sourceName = context.getSourceName();
        if (sourceName == null) {
            throw new IllegalArgumentException("No source name provided");
        }

        char pathSeparator = resourceReference.pathSeparator();
        String[] path = resourceReference.path().split(Pattern.quote(String.valueOf(pathSeparator)));

        ModelDataList data = translationDataProvider.findTranslationData(sourceName, path, pathSeparator);

        ResourceOptions options = resourceReference.createOptions();
        if (data != null && !data.isEmpty()) {
            List<T> results = new ArrayList<>();
            int i = 0;

            for (ModelDataValue dataMap : data) {
                try {
                    List<T> formatted = formatData(
                            dataMap,
                            options,
                            resourceReference.formattingScheme(),
                            context
                    );

                    results.addAll(formatted);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to format data at index: " + i, e);
                }
                ++i;
            }
            return results;
        }

        List<T> def = (List<T>) consumeDefault((ResourceReference<Object>) resourceReference, context);
        if (def == null) {
            throw new IllegalArgumentException(String.format("No data found on source '%s' and no default resource value found using path '%s'", sourceName, resourceReference.path()));
        }
        return def;
    }

    @NotNull @Override
    public <T> ResourceMapper registerInterpreter(@NotNull Class<T> resourceClass, @NotNull ResourceInterpreter interpreter) {
        interpreterMap.put(resourceClass, interpreter);
        return this;
    }

    @Override
    public void registerStringInterceptors(@NotNull StringInterceptor... stringInterceptors) {
        this.stringInterceptors.addAll(Arrays.asList(stringInterceptors));
    }

    @NotNull @Override
    public ResourceMapper bindClassTypeName(@NotNull String typeName, @NotNull Class<?> clazz) {
        classTypeNames.put(typeName, clazz);
        return this;
    }

    @NotNull @Override
    public TranslationDataProvider translationDataProvider() {
        return translationDataProvider;
    }

    private <T> List<T> consumeDefault(ResourceReference<T> resourceReference, FormattingContext context) {
        List<T> def = resourceReference.defaultValue();
        if (def == null) {
            return null;
        }

        def = new ArrayList<>(def);
        formatValue(def, resourceReference.formattingScheme(), context);
        return def;
    }

    private <T> T formatValue(T value, FormattingScheme scheme, FormattingContext context) {
        if (value instanceof List) {
            ((List) value).replaceAll(element -> formatValue(element, scheme, context));
        } else if (value instanceof InternalFormatting) {
            Function<String, String> formatter = (s -> {
                List<String> str = formatStrings(Collections.singletonList(s), scheme, context);
                return (String) str.get(0);
            });

            ((InternalFormatting) value).acceptFormatter(formatter);
        } else if (value instanceof String) {
            return (T) formatStrings(Collections.singletonList((String) value), scheme, context).get(0);
        }
        return value;
    }
}
