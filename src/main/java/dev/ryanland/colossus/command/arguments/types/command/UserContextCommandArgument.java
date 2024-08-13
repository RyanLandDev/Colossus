package dev.ryanland.colossus.command.arguments.types.command;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import dev.ryanland.colossus.command.executor.CommandHandler;
import net.dv8tion.jda.api.entities.User;
import dev.ryanland.colossus.command.ContextCommand;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.events.command.CommandEvent;

public class UserContextCommandArgument extends ArgumentStringResolver<ContextCommand<User>> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return BasicCommandArgument.getAutocompleteChoiceData(CommandHandler.getUserContextCommands());
    }

    @Override
    public ContextCommand<User> resolve(String arg, CommandEvent event) throws ArgumentException {
        ContextCommand<User> command = CommandHandler.getUserContextCommand(arg);
        if (command == null) throw new MalformedArgumentException("This command was not found.");
        else return command;
    }
}
