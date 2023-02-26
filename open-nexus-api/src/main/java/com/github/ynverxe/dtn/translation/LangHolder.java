package com.github.ynverxe.dtn.translation;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.translation.resource.ResourceReference;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LangHolder {
    @NotNull String lang();

    @NotNull default <T> T findTranslationResource(@NotNull ResourceReference<T> typedResourceReference, @Nullable FormattingContext formattingContext) {
        if (formattingContext == null) {
            formattingContext = new FormattingContext();
        }
        final ResourceMapper resourceMapper = DestroyTheNexus.instance().resourceMapper();
        formattingContext.setReceptor(this);
        formattingContext.setSourceName(lang());
        final List<T> list = resourceMapper.formatResource(typedResourceReference, formattingContext);
        return list.get(0);
    }
}
