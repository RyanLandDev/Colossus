package dev.ryanland.colossus.command.executor.functional_interface;

import dev.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandBiPredicate<T, U> {

    boolean test(T t, U u) throws CommandException;
}
