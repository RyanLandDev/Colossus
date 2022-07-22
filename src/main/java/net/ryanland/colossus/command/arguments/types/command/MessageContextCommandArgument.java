package net.ryanland.colossus.command.arguments.types.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.events.CommandEvent;

public class MessageContextCommandArgument extends ArgumentStringResolver<ContextCommand<Message>> {

    @Override
    public ContextCommand<Message> resolve(String arg, CommandEvent event) throws ArgumentException {
        ContextCommand<Message> command = CommandHandler.getMessageContextCommand(arg);
        if (command == null) throw new MalformedArgumentException("This command was not found.");
        else return command;
    }
}
