package dev.ryanland.colossus.command.executor.functional_interface;

import dev.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandConsumer<T> {

    void accept(T t) throws CommandException;
}
