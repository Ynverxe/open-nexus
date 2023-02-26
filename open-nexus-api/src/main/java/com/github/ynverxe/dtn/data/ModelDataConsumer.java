package com.github.ynverxe.dtn.data;

import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ModelDataConsumer<T> {

    List<BiConsumer<T, ModelDataTree>> segments();

    <E> ModelDataConsumer<T> mapTree(
            Function<ModelDataTree, E> treeConsumer,
            BiConsumer<T, E> valueAppender,
            boolean optional
    );

    <E> ModelDataConsumer<T> parseAndConsume(
            String path,
            ModelDataParser<E> parser,
            BiConsumer<T, E> valueAppender,
            boolean optional
    );

    ModelDataConsumer<T> consumeData(
            String path,
            BiConsumer<T, ModelDataValue> valueAppender,
            boolean optional
    );

    void parse(ModelDataTree tree, T context);

    static <T> ModelDataConsumer<T> create(Class<T> contextType) {
        return new ModelDataConsumerImpl<>();
    }

    static <T> ModelDataConsumer<T> create() {
        return new ModelDataConsumerImpl<>();
    }
}