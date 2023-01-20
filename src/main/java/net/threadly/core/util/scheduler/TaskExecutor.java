package net.threadly.core.util.scheduler;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskExecutor extends BukkitRunnable {

    private AtomicInteger times = new AtomicInteger(0);
    private int until;
    private Runnable runnable;

    public TaskExecutor(int times, Runnable runnable) {
        this.until = times;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if(times.incrementAndGet() <= until) {
            runnable.run();
        } else {
            if(!this.isCancelled()) {
                cancel();
            }
        }
    }
}
