package dev.ryanland.colossus.command.finalizers;

import dev.ryanland.colossus.events.command.BasicCommandEvent;

/**
 * Finalizers are executed when a command has finished running without errors
 */
public interface Finalizer {

    /**
     * Code to execute after a command
     */
    void finalize(BasicCommandEvent event);
}
