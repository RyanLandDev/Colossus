package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventWaiterListener implements EventListener {

    private static final EventWaiterListener instance = new EventWaiterListener();

    public static EventWaiterListener getInstance() {
        return instance;
    }

    private final HashMap<Class<? extends Event>, List<EventWaiter>> ACTIVE = new HashMap<>();

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (ACTIVE.isEmpty()) return;
        Event event = (Event) genericEvent;
        List<EventWaiter> waiters = getWaiters(event.getClass());
        for (EventWaiter waiter : waiters) {
            if (waiter.checkCondition(event)) waiter.performAction(event);
        }
    }

    public <T extends Event> List<EventWaiter> getWaiters(Class<T> eventType) {
        return ACTIVE.getOrDefault(eventType, new ArrayList<>());
    }

    public void register(EventWaiter eventWaiter) {
        List<EventWaiter> waiters = getWaiters(eventWaiter.getEventType());
        waiters.add(eventWaiter);
        ACTIVE.put(eventWaiter.getEventType(), waiters);
    }

    public void disable(EventWaiter eventWaiter) {
        List<EventWaiter> waiters = getWaiters(eventWaiter.getEventType());
        waiters.remove(eventWaiter);
        if (waiters.isEmpty()) ACTIVE.remove(eventWaiter.getEventType());
        else ACTIVE.put(eventWaiter.getEventType(), waiters);
    }

}
