package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandConsumer<T> {

    void accept(T t) throws CommandException;
}
