package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

/**
 * Inhibitors are run before a command is executed.<br>
 * They can stop execution of the command if a condition is not met, such as the user not having enough permissions.
 * @see net.ryanland.colossus.ColossusBuilder#registerInhibitors(Inhibitor...)
 */
public interface Inhibitor {

    boolean check(CommandEvent event);

    PresetBuilder buildMessage(CommandEvent event);
}
