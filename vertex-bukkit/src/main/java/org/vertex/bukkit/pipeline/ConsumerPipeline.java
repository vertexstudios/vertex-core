package org.vertex.bukkit.pipeline;

import java.util.function.Consumer;

public class ConsumerPipeline<T> extends AbstractPipeline<Consumer<T>> {

    public void accept(T arg) {
        before.accept(arg);
        while(!pipeline.isEmpty()) {
            Consumer<T> action = pipeline.poll();
            action.accept(arg);
        }
        then.accept(arg);
    }
}
