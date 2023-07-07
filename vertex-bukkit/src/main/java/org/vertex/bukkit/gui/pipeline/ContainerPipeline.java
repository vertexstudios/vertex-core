package org.vertex.bukkit.gui.pipeline;

import java.util.Queue;
import java.util.function.Consumer;

import org.vertex.bukkit.gui.Container;

import lombok.Getter;
import lombok.NonNull;

public class ContainerPipeline {
    @Getter @NonNull private Container parent;
    @Getter @NonNull private Queue<Consumer<Container>> pipeline;
    @Getter private Consumer<Container> before = c -> {};
    @Getter private Consumer<Container> then = c -> {} ;

    public ContainerPipeline attach(Consumer<Container> onClose) {
        pipeline.add(onClose);
        return this;
    }

    public ContainerPipeline then(Consumer<Container> then) {
        this.then = then;
        return this;
    }

    public ContainerPipeline before(Consumer<Container> before) {
        this.before = before;
        return this;
    }
    
    public void run() {
        before.accept(parent);
        while(!pipeline.isEmpty()) {
            Consumer<Container> action = pipeline.poll();
            action.accept(parent);
        }
        then.accept(parent);
    }
}
