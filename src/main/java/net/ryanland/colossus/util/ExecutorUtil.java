package net.ryanland.colossus.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorUtil {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(0);

    public static void schedule(Runnable runnable, long timeAmount, TimeUnit timeUnit) {
        SCHEDULED_EXECUTOR.schedule(runnable, timeAmount, timeUnit);
    }

}
