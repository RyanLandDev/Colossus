package dev.ryanland.colossus.command.executor.functional_interface;

import dev.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandFunction<T, R> {

    R apply(T t) throws CommandException;
}
