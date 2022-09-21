package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandBiConsumer<T, U> {

    void accept(T t, U u) throws CommandException;
}
