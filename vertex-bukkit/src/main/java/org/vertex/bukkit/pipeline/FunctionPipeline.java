package org.vertex.bukkit.pipeline;

import lombok.Getter;
import lombok.NonNull;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;
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
