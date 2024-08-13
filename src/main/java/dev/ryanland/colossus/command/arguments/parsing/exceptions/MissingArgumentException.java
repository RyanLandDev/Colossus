package dev.ryanland.colossus.command.arguments.parsing.exceptions;

public class MissingArgumentException extends ArgumentException {

    public MissingArgumentException() {
        super("Expected an argument");
    }

}
