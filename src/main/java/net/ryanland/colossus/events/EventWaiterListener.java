package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.ryanland.colossus.Colossus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class EventWaiterListener implements EventListener {

    private final Class<Event> eventClass;
    private final Predicate<Event> condition;
    private final BiConsumer<Event, EventWaiterListener> action;

    private boolean isActive;

    /**
     * Creates an {@link EventWaiterListener}.<br>
     * You can use the helper class {@link EventWaiter} to create an Event Waiter
     * @see EventWaiter
     */
    @SuppressWarnings("all")
    public <T extends Event> EventWaiterListener(Class<T> eventClass, Predicate<T> condition,
                                                 BiConsumer<T, EventWaiterListener> action) {
        this.eventClass = (Class<Event>) eventClass;
        this.condition = (Predicate<Event>) condition;
        this.action = (BiConsumer<Event, EventWaiterListener>) action;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Start listening for the pre-defined event
     * @see #stop()
     */
    public void start() {
        if (isActive()) throw new IllegalStateException("This listener is already active");
        Colossus.getJDA().addEventListener(this);
        isActive = true;
    }

    /**
     * Stop listening
     * @see #start()
     */
    public void stop() {
        if (!isActive()) throw new IllegalStateException("This listener has already stopped");
        Colossus.getJDA().removeEventListener(this);
        isActive = false;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (eventClass.isAssignableFrom(event.getClass())) {
            System.out.println("yes1");
            if (condition.test((Event) event)) {
                System.out.println("yes2");
                action.accept((Event) event, this);
            }
        } else {
            System.out.println("no");
        }
    }
}
