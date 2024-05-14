package org.vertex.bukkit.pipeline;

import java.util.function.Predicate;

public class PredicatePipeline<T> extends AbstractPipeline<Predicate<T>> {

    public boolean test (T arg) {
        if(before != null && !before.test(arg)) {
            return false;
        }
        while(!pipeline.isEmpty()) {
            Predicate<T> action = pipeline.poll();
            if(!action.test(arg)) {
                return false;
            }
        }
        if(then != null) {
            return then.test(arg);
        }
        return true;
    }

}
