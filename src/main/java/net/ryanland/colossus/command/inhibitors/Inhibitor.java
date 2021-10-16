package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.events.ContentCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

/**
 * Inhibitors are run before a command is executed.
 * They can stop execution of the command is a condition is not met, such as the user not having enough permissions.
 */
public interface Inhibitor {

    boolean check(ContentCommandEvent event);

    PresetBuilder buildMessage(ContentCommandEvent event);
}
