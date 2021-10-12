package net.ryanland.colossus.bot.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.Argument;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.events.CommandEvent;

import java.util.Deque;

public abstract class SingleArgument<T> extends Argument<T> {

    @Override
    public T parseArg(Deque<OptionMapping> arguments, CommandEvent event) throws ArgumentException {
        return parsed(arguments.pop(), event);
    }

    public abstract T parsed(OptionMapping argument, CommandEvent event) throws ArgumentException;
}
