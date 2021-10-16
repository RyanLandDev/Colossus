package net.ryanland.colossus.command.arguments.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.ContentCommandEvent;

public class CommandArgument extends SingleArgument<Command> {

    @Override
    public Command parsed(OptionMapping argument, ContentCommandEvent event) throws ArgumentException {
        Command command = CommandHandler.getCommand(argument.getAsString());

        if (command == null) {
            throw new MalformedArgumentException(
                "This command was not found."
            );
        } else {
            return command;
        }
    }
}
