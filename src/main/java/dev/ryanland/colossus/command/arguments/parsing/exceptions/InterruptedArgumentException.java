package dev.ryanland.colossus.command.arguments.parsing.exceptions;

public class InterruptedArgumentException extends ArgumentException {

    public InterruptedArgumentException() {
        super("Future argument interruption error: please try again and report this to a developer.");
        //super("Argument interrupted, please try again.");
    }

}
