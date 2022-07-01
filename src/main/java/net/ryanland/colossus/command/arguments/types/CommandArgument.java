package net.ryanland.colossus.command.arguments.types;

import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.events.CommandEvent;

public class CommandArgument extends ArgumentStringResolver<Command> {

    private Command findCommand(String name) throws ArgumentException {
        Command command = CommandHandler.getCommand(name);
        if (command == null)
            throw new MalformedArgumentException("This command was not found.");
        else
            return command;
    }

    @Override
    public Command resolve(String arg, CommandEvent event) throws ArgumentException {
        return findCommand(arg);
    }
}
