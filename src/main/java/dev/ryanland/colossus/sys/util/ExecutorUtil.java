package dev.ryanland.colossus.sys.util;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for executors
 */
public class ExecutorUtil {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(0);
    private static final HashMap<String, Future<?>> TASKS = new HashMap<>();

    /**
     * Schedule a task
     * @param id Provide an ID which can later be used to identify this task, may be {@code null}
     * @param runnable The code to execute
     * @param timeAmount The amount of time before the runnable should be executed
     * @param timeUnit The time unit
     * @see #cancel(String, boolean)
     * @see #getActiveTask(String)
     */
    public static void schedule(@Nullable String id, Runnable runnable, long timeAmount, TimeUnit timeUnit) {
        if (id == null) SCHEDULED_EXECUTOR.schedule(runnable, timeAmount, timeUnit);
        else {
            TASKS.put(id, SCHEDULED_EXECUTOR.schedule(() -> {
                    TASKS.remove(id);
                    runnable.run();
                }, timeAmount, timeUnit)
            );
        }
    }

    /**
     * Cancel an active task that is currently executing or awaiting to be executed
     * @param id The previously provided ID of the task
     * @param mayInterruptIfRunning If {@code true}, interrupt in-progress tasks, otherwise finish the task
     */
    public static void cancel(String id, boolean mayInterruptIfRunning) {
        var task = TASKS.remove(id);
        if (task != null) task.cancel(mayInterruptIfRunning);
    }

    /**
     * Gets an active task using the previously provided ID
     */
    public static Future<?> getActiveTask(String id) {
        return TASKS.get(id);
    }

}
