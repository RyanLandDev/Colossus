package net.ryanland.colossus.command.regular;

import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;

public interface CombinedCommand extends SlashCommand, MessageCommand {

    @Override
    default void run(MessageCommandEvent event) throws CommandException {
        execute(event);
    }

    @Override
    default void run(SlashCommandEvent event) throws CommandException {
        execute(event);
    }

    void execute(CommandEvent event) throws CommandException;
}
