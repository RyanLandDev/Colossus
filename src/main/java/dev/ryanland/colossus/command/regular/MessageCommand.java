package dev.ryanland.colossus.command.regular;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;

public interface MessageCommand {

    void run(MessageCommandEvent event) throws CommandException;
}
