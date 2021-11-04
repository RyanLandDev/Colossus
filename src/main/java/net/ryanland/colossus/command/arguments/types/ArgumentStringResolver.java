package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public abstract class ArgumentStringResolver<T> extends SingleArgument<T> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public T resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return resolve(arg.getAsString(), event);
    }

    @Override
    public T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return resolve(arg, event);
    }

    public abstract T resolve(String arg, CommandEvent event) throws ArgumentException;
}
