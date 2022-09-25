package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandFunction<T, R> {

    R apply(T t) throws CommandException;
}
