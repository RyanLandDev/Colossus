package net.ryanland.colossus.command.arguments.parsing.exceptions;

import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.ContentCommandEvent;

public class ArgumentException extends CommandException {

    private final String message;

    public ArgumentException(String message) {
        super(message);
        this.message = message;
    }

    public String getRawMessage() {
        return message;
    }

    public String getMessage(ContentCommandEvent event, Argument<?> argument) {
        return HelpMaker.formattedUsage(event, argument)
            + "\n\n" + message;
    }
}
