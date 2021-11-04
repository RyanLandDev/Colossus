package net.ryanland.colossus.command.arguments.old.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;

import java.util.Deque;

public abstract class SingleArgument<T> extends Argument<T> {

    @Override
    public T parseArg(Deque<OptionMapping> arguments, MessageCommandEvent event) throws ArgumentException {
        return parsed(arguments.pop(), event);
    }

    public abstract T parsed(OptionMapping argument, MessageCommandEvent event) throws ArgumentException;
}
