package dev.ryanland.colossus.command.arguments.types.command;

import dev.ryanland.colossus.command.ContextCommand;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.events.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public class MessageContextCommandArgument extends ArgumentStringResolver<ContextCommand<Message>> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return BasicCommandArgument.getAutocompleteChoiceData(CommandHandler.getMessageContextCommands());
    }

    @Override
    public ContextCommand<Message> resolve(String arg, CommandEvent event) throws ArgumentException {
        ContextCommand<Message> command = CommandHandler.getMessageContextCommand(arg);
        if (command == null) throw new MalformedArgumentException("This command was not found.");
        else return command;
    }
}
