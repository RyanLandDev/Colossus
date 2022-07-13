package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.Event;
import net.ryanland.colossus.sys.ExecutorUtil;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class EventWaiter {

    /**
     * Starts listening for a specific event and runs an action whenever the event is fired and the condition is met.
     * @param eventClass The class of the event to listen for
     * @param condition The condition the event has to meet in order to run the action
     * @param action The action to run when the event is fired and the condition is met
     * @param runOnce If {@code true}, stops listening for the event after it has been successfully run once
     * @param <T> The event type
     * @return The event listener object, you can e.g. use this to stop listening early
     * @see #awaitEvent(Class, Predicate, BiConsumer, boolean, long, TimeUnit, Runnable)
     * @see EventWaiterListener
     */
    @SuppressWarnings("all")
    public static <T extends Event> EventWaiterListener awaitEvent(Class<T> eventClass, Predicate<T> condition,
                                                                   BiConsumer<T, EventWaiterListener> action, boolean runOnce) {
        EventWaiterListener listener = new EventWaiterListener(eventClass, condition, (event, listener1) -> {
            if (runOnce) listener1.stop();
            action.accept(event, listener1);
        });
        listener.start();
        return listener;
    }

    /**
     * Starts listening for a specific event and runs an action whenever the event is fired and the condition is met.
     * <br>
     * Additionally, after the specified amount of time has passed, stops listening and performs the given timeout action,
     * if the listener is still active at this point.
     * @param eventClass The class of the event to listen for
     * @param condition The condition the event has to meet in order to run the action
     * @param action The action to run when the event is fired and the condition is met
     * @param runOnce If {@code true}, stops listening for the event after it has been successfully run once
     * @param timeAmount The amount of time before the listener is stopped and the timeout action is ran
     * @param timeUnit The time unit
     * @param timeoutAction The timeout action
     * @param <T> The event type
     * @return The event listener object, you can e.g. use this to stop listening early
     * @see #awaitEvent(Class, Predicate, BiConsumer, boolean)
     * @see EventWaiterListener
     */
    @SuppressWarnings("all")
    public static <T extends Event> EventWaiterListener awaitEvent(Class<T> eventClass, Predicate<T> condition,
                                                                   BiConsumer<T, EventWaiterListener> action, boolean runOnce,
                                                                   long timeAmount, TimeUnit timeUnit,
                                                                   @Nullable Runnable timeoutAction) {
        EventWaiterListener listener = awaitEvent(eventClass, condition, action, runOnce);
        ExecutorUtil.schedule(null, () -> {
            if (listener.isActive()) {
                listener.stop();
                if (timeoutAction != null) timeoutAction.run();
            }
        }, timeAmount, timeUnit);
        return listener;
    }
}
