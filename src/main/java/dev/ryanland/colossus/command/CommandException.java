package dev.ryanland.colossus.command;

public class CommandException extends Exception {
    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }
}
