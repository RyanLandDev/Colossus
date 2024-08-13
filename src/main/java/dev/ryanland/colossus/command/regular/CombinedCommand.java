package dev.ryanland.colossus.command.regular;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;

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
