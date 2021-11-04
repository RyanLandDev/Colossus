package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.Deque;

public abstract class SingleArgument<T> extends Argument<T> {

    @Override
    public T resolveSlashCommandArgument(Deque<OptionMapping> args, SlashEvent event) throws ArgumentException {
        return resolveSlashCommandArgument(args.pop(), event);
    }

    @Override
    public T resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        return resolveMessageCommandArgument(args.pop(), event);
    }

    public abstract T resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException;

    public abstract T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException;
}
