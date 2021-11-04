package net.ryanland.colossus.command;

import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public interface CombinedCommand extends SlashCommand, MessageCommand {

    @Override
    default void run(MessageCommandEvent event) throws CommandException {
        execute(event);
    }

    @Override
    default void run(SlashEvent event) throws CommandException {
        execute(event);
    }

    void execute(CommandEvent event) throws CommandException;
}
