package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.events.RepliableEvent;

public interface Finalizer<T extends RepliableEvent> {

    void finalize(T event);
}
