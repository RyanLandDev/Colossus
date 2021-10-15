package net.ryanland.colossus.bot.command.finalizers;

import net.ryanland.colossus.bot.events.CommandEvent;

/**
 * Command finalizers are executed when a command has finished running without errors
 */
public interface Finalizer {

    void finalize(CommandEvent event);
}
