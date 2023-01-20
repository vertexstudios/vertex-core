package net.threadly.core.util.scheduler;

import net.threadly.core.PluginContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class Scheduler {


    public static Scheduler create() {
        return new Scheduler();
    }

    private int times = 0;
    private long every = 0;
    private long startAfter = 0;
    private Runnable runnable;
    private boolean async = false;

    public Scheduler times(int times) {
        this.times = times;
        return this;
    }

    public Scheduler async() {
        this.async = true;
        return this;
    }

    public Scheduler every(long value, TimeUnit unit) {
        this.every = TimeUnit.SECONDS.convert(value, unit) * 20;
        return this;
    }

    public Scheduler startAfter(long value, TimeUnit unit) {
        this.startAfter = TimeUnit.SECONDS.convert(value, unit) * 20;
        return this;
    }

    public Scheduler trigger(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public BukkitTask execute() {
        Method method = null;
        Object[] args;

        if(times != 0 && every == 0) {
            return null;
        }

        try {
            if (every == 0) {
                if (startAfter == 0) {
                    String methodName = async ? "runTaskAsynchronously" : "runTask";
                    method = BukkitScheduler.class.getDeclaredMethod(methodName, Plugin.class, Runnable.class);
                    args = new Object[]{PluginContainer.getCurrentPlugin(), runnable};
                } else {
                    String methodName = async ? "runTaskLaterAsynchronously" : "runTaskLater";
                    method = BukkitScheduler.class.getDeclaredMethod(methodName, Plugin.class, Runnable.class, long.class);
                    args = new Object[]{PluginContainer.getCurrentPlugin(), runnable, startAfter};
                }
            } else {
                if(times == 0) {
                    String methodName = async ? "runTaskTimerAsynchronously" : "runTaskTimer";
                    method = BukkitScheduler.class.getDeclaredMethod(methodName, Plugin.class, Runnable.class, long.class, long.class);
                    args = new Object[]{PluginContainer.getCurrentPlugin(), runnable, startAfter, every};
                } else {
                    BukkitRunnable task = new TaskExecutor(times, runnable);
                    if(async) {
                        return task.runTaskTimerAsynchronously(PluginContainer.getCurrentPlugin(), startAfter, every);
                    } else {
                        return task.runTaskTimer(PluginContainer.getCurrentPlugin(), startAfter, every);
                    }
                }
            }

            return (BukkitTask) method.invoke(Bukkit.getScheduler(), args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}
