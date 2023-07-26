package org.vertex.bukkit.pipeline;

import lombok.Getter;
import lombok.NonNull;
import org.vertex.bukkit.gui.Container;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
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
