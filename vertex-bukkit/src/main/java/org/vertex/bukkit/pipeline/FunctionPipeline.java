package org.vertex.bukkit.pipeline;

import java.util.function.Function;

public class FunctionPipeline<T> extends AbstractPipeline<Function<T,T>> {

    public T apply(T value) {
        T object = this.before.apply(value);
        while(!pipeline.isEmpty()) {
            Function<T,T> action = pipeline.poll();
            object = action.apply(object);
        }
        return this.then.apply(object);
    }

}
