package dev.ryanland.colossus.command.arguments.types.command;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.events.command.CommandEvent;

public class CommandArgument extends ArgumentStringResolver<Command> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return BasicCommandArgument.getAutocompleteChoiceData(CommandHandler.getCommands());
    }

    @Override
    public Command resolve(String arg, CommandEvent event) throws ArgumentException {
        Command command = CommandHandler.getCommand(arg);
        if (command == null) throw new MalformedArgumentException("This command was not found.");
        else return command;
    }
}
