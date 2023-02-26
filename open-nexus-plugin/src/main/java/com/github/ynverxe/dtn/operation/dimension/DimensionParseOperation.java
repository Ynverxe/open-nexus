package com.github.ynverxe.dtn.operation.dimension;

import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.context.DimensionParsingContext;
import com.github.ynverxe.dtn.data.ModelDataConsumer;
import com.github.ynverxe.dtn.operation.AbstractOperation;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.logging.Level;

public class DimensionParseOperation<T extends DimensionParsingContext> extends AbstractOperation<Void> {

    private final ModelDataConsumer<T> consumer;
    private final T context;

    public DimensionParseOperation(ModelDataConsumer<T> consumer, T context, boolean catchException) {
        super(catchException);
        this.consumer = consumer;
        this.context = context;

        setFailHandler(throwable -> {
            DestroyTheNexus.LOGGER.log(Level.SEVERE, "Cannot parse dimension '{0}'", new Object[]{context.getDimension().name()});
            for (World world : context.getWorldContainer()) {
                Bukkit.unloadWorld(world, false);
            }
        });
    }

    @Override
    protected Void run() throws Throwable {
        consumer.parse(context.getPropertiesContainer(), context);
        return null;
    }
}