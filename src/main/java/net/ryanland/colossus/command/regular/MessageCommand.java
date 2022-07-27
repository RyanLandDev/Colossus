package net.ryanland.colossus.command.regular;

import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.command.MessageCommandEvent;

public interface MessageCommand {

    void run(MessageCommandEvent event) throws CommandException;
}
