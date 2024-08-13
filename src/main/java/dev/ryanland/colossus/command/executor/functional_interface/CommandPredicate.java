package dev.ryanland.colossus.command.executor.functional_interface;

import dev.ryanland.colossus.command.CommandException;

@FunctionalInterface
public interface CommandPredicate<T> {

    boolean test(T t) throws CommandException;
}
