package net.threadly.core.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String usage();

    CommandParam[] params() default {};

    String permission() default "";

    boolean playerOnly() default false;

    @interface CommandParam {
        String key();
        Class<?> type();
        String conversor();
    }

}
