package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.events.ContextCommandEvent;

/**
 * Context inhibitors are run before a context command is executed.<br>
 * They can stop execution of the command if a condition is not met, such as the user not having enough permissions.
 * @see net.ryanland.colossus.ColossusBuilder#registerContextInhibitors(ContextInhibitor...)
 */
public non-sealed interface ContextInhibitor extends Inhibitor<ContextCommandEvent<?>> {
}
