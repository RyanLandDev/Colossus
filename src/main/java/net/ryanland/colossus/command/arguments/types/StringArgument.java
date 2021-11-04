package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class StringArgument extends SingleArgument<String> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public String resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return arg.getAsString();
    }

    @Override
    public String resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return arg;
    }
}
