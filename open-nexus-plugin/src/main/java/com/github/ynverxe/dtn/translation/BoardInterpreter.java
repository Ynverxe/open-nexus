package com.github.ynverxe.dtn.translation;

import com.github.ynverxe.structured.data.ModelDataValue;
import org.jetbrains.annotations.Nullable;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.translation.resource.mapping.ResourceMapper;
import com.github.ynverxe.translation.resource.mapping.FormattingContext;
import com.github.ynverxe.translation.resource.mapping.FormattingScheme;
import org.jetbrains.annotations.NotNull;
import com.github.ynverxe.translation.resource.mapping.ResourceInterpreter;

public class BoardInterpreter implements ResourceInterpreter {
    @Nullable
    public Object buildResource(
            @NotNull ModelDataValue value,
            @NotNull FormattingScheme formattingScheme,
            @NotNull FormattingContext formattingContext,
            @NotNull ResourceMapper resourceMapper
    ) {
        return BoardModel.deserialize(value.asTree().simplify());
    }
}
