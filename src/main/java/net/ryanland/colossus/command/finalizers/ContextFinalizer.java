package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.events.ContextCommandEvent;

/**
 * Context finalizers are executed when a context command has finished running without errors
 */
public interface ContextFinalizer extends Finalizer<ContextCommandEvent<?>> {
}
