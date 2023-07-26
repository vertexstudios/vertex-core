package org.vertex.bukkit.pipeline;

public class RunnablePipeline extends AbstractPipeline<Runnable> {

    public void run() {
        before.run();
        while(!pipeline.isEmpty()) {
            Runnable action = pipeline.poll();
            action.run();
        }
        then.run();
    }
}
