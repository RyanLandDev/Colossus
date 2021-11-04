package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class BooleanArgument extends SingleArgument<Boolean> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.BOOLEAN;
    }

    @Override
    public Boolean resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return arg.getAsBoolean();
    }

    @Override
    public Boolean resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return Boolean.parseBoolean(arg);
    }
}
