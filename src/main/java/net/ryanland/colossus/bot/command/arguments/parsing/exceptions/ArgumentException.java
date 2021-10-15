package net.ryanland.colossus.bot.command.arguments.parsing.exceptions;

import net.ryanland.colossus.bot.command.arguments.Argument;
import net.ryanland.colossus.bot.command.CommandException;
import net.ryanland.colossus.bot.command.info.HelpMaker;
import net.ryanland.colossus.bot.events.CommandEvent;

public class ArgumentException extends CommandException {

    private final String message;

    public ArgumentException(String message) {
        super(message);
        this.message = message;
    }

    public String getRawMessage() {
        return message;
    }

    public String getMessage(CommandEvent event, Argument<?> argument) {
        return HelpMaker.formattedUsage(event, argument)
            + "\n\n" + message;
    }
}
