package org.vertex.bukkit.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.vertex.bukkit.BukkitPluginContainer;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Handler<T extends Event> {

    @Getter @NotNull private Class<T> event;
    @Getter @Builder.Default private EventPriority priority = EventPriority.NORMAL;
    @Getter @Builder.Default private boolean ignoreCancelled = false;
    @Getter @NotNull private HandlerConsumer<T> handler = (c) -> {};

    public void register() {

        Listener listener = new Listener() {
            @EventHandler
            public void handle(T event) {
                handler.handle(event);
            }
        };

        try {
            Method method = listener.getClass().getDeclaredMethod("handle", event);
            EventHandler oldAnnotation = (EventHandler) method.getDeclaredAnnotations()[0];
            Annotation annotation = new EventHandler() {
                @Override
                public EventPriority priority() {
                    return priority;
                }
                
                @Override
                public boolean ignoreCancelled() {
                    return ignoreCancelled;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return oldAnnotation.annotationType();
                }
            };
            Field field = listener.getClass().getDeclaredField("annotations");
            field.setAccessible(true);
    Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field.get(Foobar.class);
    annotations.put(Something.class, newAnnotation);
        }

        Annotation annotation = new EventHandler() {
            @Override
            public EventPriority priority() {
                return priority;
            }
            
            @Override
            public boolean ignoreCancelled() {
                return ignoreCancelled;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return oldAnnotation.annotationType();
            }
        };

        try {
            annotation = listener.getClass().getDeclaredMethod("handle", event).getDeclaredAnnotation(EventHandler.class);
            annotation.
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            
            @EventHandler(
                ignoreCancelled = fa ,
                priority = priority
            )
            public void handle(T event) {
                handler.handle(event);
            }

        }, BukkitPluginContainer.getCurrentPlugin());
    }


}
