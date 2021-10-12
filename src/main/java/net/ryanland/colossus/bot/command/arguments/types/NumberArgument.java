package net.ryanland.colossus.bot.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.bot.events.CommandEvent;

import java.util.Deque;

public abstract class NumberArgument<T> extends SingleArgument<T> {

    @Override
    public T parseArg(Deque<OptionMapping> arguments, CommandEvent event) throws ArgumentException {
        try {
            return parsed(arguments.remove(), event);
        } catch (NumberFormatException e) {
            throw new MalformedArgumentException("Invalid number provided.");
        }
    }

    public abstract T parsed(OptionMapping argument, CommandEvent event) throws ArgumentException;
}
