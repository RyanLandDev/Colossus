package dev.ryanland.colossus.command.inhibitors;

import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import dev.ryanland.colossus.events.command.BasicCommandEvent;

/**
 * Inhibitors are run before a command is executed.<br>
 * They can stop execution of the command if a condition is not met, such as the user not having enough permissions.
 * @see ColossusBuilder#registerInhibitors(Inhibitor...)
 */
public interface Inhibitor {

    /**
     * Returns {@code true} if the inhibitor was not accepted, resulting in the command not being executed
     * and the message defined in {@link #buildMessage(BasicCommandEvent)} being sent.<br>
     * Returns {@code false} otherwise.
     */
    boolean check(BasicCommandEvent event);

    /**
     * Returns the error message sent in case the inhibitor is not accepted
     */
    PresetBuilder buildMessage(BasicCommandEvent event);
}
