package net.ryanland.colossus.bot.command.executor.functional_interface;

import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;

import java.util.function.Predicate;

@FunctionalInterface
public interface CommandPredicate<T> extends Predicate<T> {

    @Override
    default boolean test(T t) {
        throw new IllegalStateException("Use CommandPredicate#check instead");
    }

    boolean check(T t) throws CommandException;
}
