package net.ryanland.colossus.command.executor.functional_interface;

import net.ryanland.colossus.command.CommandException;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface CommandBiPredicate<T, U> extends BiPredicate<T, U> {

    @Override
    default boolean test(T t, U u) {
        throw new IllegalStateException("Use CommandBiPredicate#check instead");
    }

    boolean check(T t, U u) throws CommandException;
}
