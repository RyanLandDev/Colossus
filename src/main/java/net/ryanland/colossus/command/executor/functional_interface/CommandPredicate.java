package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.Predicate;

@FunctionalInterface
public interface CommandPredicate<T> extends Predicate<T> {

    @Override
    default boolean test(T t) {
        throw new IllegalStateException("Use CommandPredicate#check instead");
    }

    boolean check(T t) throws CommandException;
}
