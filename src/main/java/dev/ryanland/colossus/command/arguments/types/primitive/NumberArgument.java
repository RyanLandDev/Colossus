package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.SingleArgument;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public abstract class NumberArgument<T> extends SingleArgument<T> {

    @Override
    public T resolveSlashCommandArg(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        try {
            return resolveSlashCommandArg(event, arg);
        } catch (NumberFormatException e) {
            throw new MalformedArgumentException("Invalid number provided.");
        }
    }

    @Override
    public T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        try {
            return resolveMessageCommandArgument(event, arg);
        } catch (NumberFormatException e) {
            throw new MalformedArgumentException("Invalid number provided.");
        }
    }

    public abstract T resolveSlashCommandArg(SlashCommandEvent event, OptionMapping arg) throws ArgumentException;

    public abstract T resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException;
}
