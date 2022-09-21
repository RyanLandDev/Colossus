package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.Predicate;

@FunctionalInterface
public interface CommandPredicate<T> {

    boolean test(T t) throws CommandException;
}
