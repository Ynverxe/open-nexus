package com.github.ynverxe.dtn.data;

import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import com.github.ynverxe.dtn.exception.ValueParseException;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import com.github.ynverxe.structured.exception.PathHolderException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unchecked, rawtypes")
public class ModelDataConsumerImpl<T> implements ModelDataConsumer<T> {

    private final List<Consumer> segmentList = new ArrayList<>();

    @Override
    public void parse(ModelDataTree tree, T context) {
        for (Consumer consumer : segmentList) {
            String path = "";

            if (consumer instanceof DirectedConsumer) path = ((DirectedConsumer<?>) consumer).path;

            try {
                consumer.accept(context, tree);
            } catch (Exception e) {
                PathHolderException pathHolderException = catchException(path, e);

                ExceptionCatcher.instance().handleException(pathHolderException, consumer.optional);
            }
        }
    }

    @Override
    public List<BiConsumer<T, ModelDataTree>> segments() {
        return Collections.unmodifiableList(segmentList);
    }

    @Override
    public <E> ModelDataConsumer<T> mapTree(
            Function<ModelDataTree, E> treeConsumer,
            BiConsumer<T, E> valueAppender,
            boolean optional
    ) {
        segmentList.add(new Consumer(optional) {
            @Override
            public void accept(T t, ModelDataTree modelDataTree) {
                E parsed = treeConsumer.apply(modelDataTree);
                valueAppender.accept(t, parsed);
            }
        });

        return this;
    }

    @Override
    public <E> ModelDataConsumer<T> parseAndConsume(
            String path,
            ModelDataParser<E> parser,
            BiConsumer<T, E> resultAppender,
            boolean optional
    ) {
        segmentList.add(new DirectedConsumer<>(path, parser, resultAppender, optional));
        return this;
    }

    @Override
    public ModelDataConsumer<T> consumeData(String path, BiConsumer<T, ModelDataValue> dataConsumer, boolean optional) {
        segmentList.add(new DirectedConsumer<>(path, null, dataConsumer, optional));
        return this;
    }

    private static PathHolderException catchException(String path, Exception e) {
        if (e instanceof PathHolderException) {
            PathHolderException pathHolderException = (PathHolderException) e;

            if (!path.isEmpty() && pathHolderException.getPath() == null)
                pathHolderException.setPath(path);

            return pathHolderException;
        } else {
            return new ValueParseException(e).setPath(path);
        }
    }

    private abstract class Consumer implements BiConsumer<T, ModelDataTree> {
        private final boolean optional;

        private Consumer(boolean optional) {
            this.optional = optional;
        }
    }

    private class DirectedConsumer<E> extends Consumer {

        private final String path;
        private final ModelDataParser<E> parser;
        private final BiConsumer resultAppender;

        public DirectedConsumer(String path, ModelDataParser<E> parser, BiConsumer resultAppender, boolean optional) {
            super(optional);
            this.path = path;
            this.parser = parser;
            this.resultAppender = resultAppender;
        }

        @Override
        public void accept(T context, ModelDataTree modelDataTree) {
            ModelDataValue value = modelDataTree.getValue(path);

            Object toConsume = value;

            if (parser != null) {
                toConsume = parser.parseDataValue(value);
            }

            resultAppender.accept(context, toConsume);
        }
    }
}