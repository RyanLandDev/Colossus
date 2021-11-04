package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class IntegerArgument extends NumberArgument<Integer> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.INTEGER;
    }

    @Override
    public Integer resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return (int) arg.getAsLong();
    }

    @Override
    public Integer resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return Integer.parseInt(arg);
    }
}
