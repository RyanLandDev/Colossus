package dev.ryanland.colossus.command.arguments.parsing.exceptions;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.info.HelpMaker;
import dev.ryanland.colossus.events.command.CommandEvent;

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
