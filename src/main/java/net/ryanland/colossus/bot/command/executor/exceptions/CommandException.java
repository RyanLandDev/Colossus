package net.ryanland.colossus.bot.command.executor.exceptions;

public class CommandException extends Exception {
    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }
}
