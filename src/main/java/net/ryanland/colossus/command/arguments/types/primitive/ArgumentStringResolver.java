package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;

public abstract class ArgumentStringResolver<T> extends SingleArgument<T> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.STRING);
    }

    @Override
    public final T resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return resolve(arg.getAsString(), event);
    }

    @Override
    public final T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return resolve(arg, event);
    }

    public abstract T resolve(String arg, CommandEvent event) throws ArgumentException;
}
