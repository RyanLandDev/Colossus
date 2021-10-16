package net.ryanland.colossus.command;

import net.ryanland.colossus.events.MessageCommandEvent;

public interface MessageCommand {

    void run(MessageCommandEvent event) throws CommandException;
}
