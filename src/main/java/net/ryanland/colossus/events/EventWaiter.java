package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.Event;
import net.ryanland.colossus.sys.ExecutorUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventWaiter {

    private final Class<Event> eventType;
    private final Predicate<Event> condition;
    private final Consumer<Event> action;

    @SuppressWarnings("unchecked")
    public <T extends Event> EventWaiter(Class<T> eventType, Predicate<T> condition, Consumer<T> action) {
        this.eventType = (Class<Event>) eventType;
        this.condition = (Predicate<Event>) condition;
        this.action = (Consumer<Event>) action;
        EventWaiterListener.getInstance().register(this);
    }

    public <T extends Event> EventWaiter(Class<T> eventType, Predicate<T> condition, Consumer<T> action,
                       long timeAmount, TimeUnit timeUnit, Runnable timeoutAction) {
        this(eventType, condition, action);
        ExecutorUtil.schedule(null, () -> {
            EventWaiterListener.getInstance().disable(this);
            if (timeoutAction != null) timeoutAction.run();
        }, timeAmount, timeUnit);
    }

    public Class<Event> getEventType() {
        return eventType;
    }

    public boolean checkCondition(Event event) {
        return condition.test(event);
    }

    public void performAction(Event event) {
        action.accept(event);
    }
}
