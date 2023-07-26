package org.vertex.bukkit.pipeline;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

import com.google.common.util.concurrent.Runnables;
import org.vertex.bukkit.gui.Container;

import lombok.Getter;
import lombok.NonNull;

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
