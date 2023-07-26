package org.vertex.bukkit.pipeline;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicatePipeline<T> extends AbstractPipeline<Predicate<T>> {

    public boolean test (T arg) {
        if(!before.test(arg)) {
            return false;
        }
        while(!pipeline.isEmpty()) {
            Predicate<T> action = pipeline.poll();
            if(!action.test(arg)) {
                return false;
            }
        }
        return then.test(arg);
    }

}
