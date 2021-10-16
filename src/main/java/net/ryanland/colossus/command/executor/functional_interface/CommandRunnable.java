package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandRunnable extends Runnable {

    @Override
    default void run() {
        throw new IllegalStateException("Use CommandRunnable#execute instead");
    }

    void execute() throws CommandException;
}
