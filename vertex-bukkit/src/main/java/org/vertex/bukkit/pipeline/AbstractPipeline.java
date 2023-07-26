package org.vertex.bukkit.pipeline;

import lombok.Getter;
import lombok.NonNull;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractPipeline<T> {

    @Getter @NonNull protected Queue<T> pipeline = new LinkedBlockingQueue<>();
    @Getter protected T before;
    @Getter protected T then;

    public AbstractPipeline attach(T t) {
        pipeline.add(t);
        return this;
    }

    public AbstractPipeline then(T t) {
        this.then = t;
        return this;
    }

    public AbstractPipeline before(T t) {
        this.before = t;
        return this;
    }

}
